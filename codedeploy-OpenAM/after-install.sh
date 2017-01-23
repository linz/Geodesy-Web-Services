#!/usr/bin/env bash

. ${BASH_SOURCE%/*}/env.sh

# Apply subsititions to config file
OPENAM_CONFIG_FILE=/opt/openam/tools/config/config.file
sed -i 's/${OPENAM_SERVER_FQDN}/'"${OPENAM_SERVER_FQDN}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_ADMIN_PWD}/'"${OPENAM_ADMIN_PWD}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_AMLDAPUSERPASSWD}/'"${OPENAM_AMLDAPUSERPASSWD}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_DS_DIRMGRPASSWD}/'"${OPENAM_DS_DIRMGRPASSWD}/" $OPENAM_CONFIG_FILE


# Apply subsititions to scripts
EXPORT_SERVICE_CONFIG_SCRIPT=/opt/openam/tools/admin/scripts/export-service-config.sh
EXPORT_DIRECTORY_DATA_SCRIPT=/opt/openam/tools/admin/scripts/export-ldif-directory-data.sh
sed -i 's/${OPENAM_SERVER_FQDN}/'"${OPENAM_SERVER_FQDN}/" $EXPORT_SERVICE_CONFIG_SCRIPT
sed -i 's/${OPENAM_SERVER_FQDN}/'"${OPENAM_SERVER_FQDN}/" $EXPORT_DIRECTORY_DATA_SCRIPT
sed -i 's/${OPENAM_DS_DIRMGRPASSWD}/'"${OPENAM_DS_DIRMGRPASSWD}/" $EXPORT_DIRECTORY_DATA_SCRIPT


# Wait if load balancer is failing health check (Configuration process requires http access to the
# OpenAM application via the load balancer)
#COUNTER=0
#until curl -s --head --connect-timeout 2 http://${OPENAM_SERVER_FQDN}/openam/images/gradlogsides.jpg | head -n 1 | grep 200 > /dev/null
#do
#  echo "waiting 10 seconds for load balancer to resume passing requests to openam server"
#  sleep 10
#  let COUNTER=COUNTER+1
#  if [ "$COUNTER" -eq "6" ]; then
#    echo "Giving up waiting for load balancer to resume passing requests to openam server"
#    exit 1
#  fi
#done


# Ensure the hosts file resolves the FQDN to 127.0.0.1 and hostname of the server is set to the FQDN
if ! grep -q "$OPENAM_SERVER_FQDN" "/etc/hosts"; then
   echo 127.0.0.1 ${OPENAM_SERVER_FQDN} >> /etc/hosts
fi
if [[ ! "$(hostname)" == "${OPENAM_SERVER_FQDN}" ]]; then
  hostname ${OPENAM_SERVER_FQDN}
fi

# Apply the initial configuration
java -jar /opt/openam/tools/config/openam-configurator-tool-13.5.0.jar -f /opt/openam/tools/config/config.file


# Install the OpenAM command line tools (OpenAM must be running and configured)
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk.x86_64
cd /opt/openam/tools/admin
./setup -p /usr/share/tomcat8/openam -d /opt/openam/tools/admin/debug -l /opt/openam/tools/admin/log --acceptLicense
echo ${OPENAM_ADMIN_PWD} > passwdfile
chmod 400 passwdfile


# Import OpenAM service config if an export file exists
FILE_NAME=${OPENAM_SERVER_FQDN}_servicecfg.xml
S3_DIR=s3://geodesy-openam/service-config-export
BASE_DIR=/opt/openam/tools/admin
IMPORT_DIR=/tmp

FILE_COUNT=$(aws s3 ls $S3_DIR/ | grep $FILE_NAME | wc -l)

if [ $FILE_COUNT -eq 1 ]; then
  # Copy file from S3 bucket to local file system
  aws s3 cp $S3_DIR/$FILE_NAME /tmp/$FILE_NAME

  # Import file into OpenAM
  echo "y" | $BASE_DIR/openam/bin/ssoadm import-svc-cfg -e geodenc -u amadmin -f $BASE_DIR/passwdfile -X $IMPORT_DIR/$FILE_NAME

  # Restart required, wait until Tomcat startup has finished
  service tomcat8 restart
  until [ "`curl --silent --connect-timeout 1 -I http://localhost:8080 | grep 'Coyote'`" != "" ];
  do
    sleep 10
  done
else
  echo "No OpenAM service config File found with name ${FILE_NAME}, nothing to import"
fi


# Import LDAP directory data if an export file exists
FILE_NAME=${OPENAM_SERVER_FQDN}_embedded_directory.ldif
S3_DIR=s3://geodesy-openam/directory-data-export
BASE_DIR=/usr/share/tomcat8/openam/opends/bin
IMPORT_DIR=/tmp

FILE_COUNT=$(aws s3 ls $S3_DIR/ | grep $FILE_NAME | wc -l)

if [ $FILE_COUNT -eq 1 ]; then
  # Copy file from S3 bucket to local file system
  aws s3 cp $S3_DIR/$FILE_NAME /tmp/$FILE_NAME

  # Import data file into LDAP directory
  $BASE_DIR/import-ldif\
     --ldifFile /tmp/$FILE_NAME\
     --backendID userRoot\
     --hostName ${OPENAM_SERVER_FQDN}\
     --port 4444\
     --bindDN cn=Directory\ Manager\
     --bindPassword ${OPENAM_DS_DIRMGRPASSWD}\
     --trustAll\
     --noPropertiesFile
else
  echo "No LDIF file found with name ${FILE_NAME}, nothing to import for the LDAP directory"
fi


# Ensure a cronjob for exporting the OpenAM service config exists
CRONTAB=$(crontab -l)
CRONJOB="$(cat <<EOF
#  Export the OpenAM service config
0 1 * * * /opt/openam/tools/admin/scripts/export-service-config.sh
EOF
)"

if [[ ! $CRONTAB == *"export-service-config"* ]]; then
  (echo "${CRONTAB}"$'\n'"${CRONJOB}") | crontab -
fi

# Ensure a cronjob for exporting the LDAP directory data exists
CRONTAB=$(crontab -l)
CRONJOB="$(cat <<EOF

#  Export the LDAP directory data
1 1 * * * /opt/openam/tools/admin/scripts/export-ldif-directory-data.sh
EOF
)"

if [[ ! $CRONTAB == *"export-ldif-directory-data"* ]]; then
  (echo "${CRONTAB}"$'\n'"${CRONJOB}") | crontab -
fi
