#!/usr/bin/env bash

set -e

. ${BASH_SOURCE%/*}/env.sh

# Apply subsititions to config file
OPENAM_CONFIG_FILE=/opt/openam/tools/config/config.file
sed -i 's/${OPENAM_SERVER_FQDN}/'"${OPENAM_SERVER_FQDN}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_ADMIN_PWD}/'"${OPENAM_ADMIN_PWD}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_AMLDAPUSERPASSWD}/'"${OPENAM_AMLDAPUSERPASSWD}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_DS_DIRMGRPASSWD}/'"${OPENAM_DS_DIRMGRPASSWD}/" $OPENAM_CONFIG_FILE

# Apply subsititions to scripts
EXPORT_OPENAM_SCRIPT=/opt/openam/tools/admin/scripts/export_openam.sh
sed -i 's/${OPENAM_SERVER_FQDN}/'"${OPENAM_SERVER_FQDN}/" $EXPORT_OPENAM_SCRIPT
sed -i 's/${OPENAM_DS_DIRMGRPASSWD}/'"${OPENAM_DS_DIRMGRPASSWD}/" $EXPORT_OPENAM_SCRIPT

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

# Install the OpenAM command line tools (OpenAM must be running and have initial configuration)
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk.x86_64
cd /opt/openam/tools/admin
./setup -p /usr/share/tomcat8/openam -d /opt/openam/tools/admin/debug -l /opt/openam/tools/admin/log --acceptLicense
echo ${OPENAM_ADMIN_PWD} > passwdfile
chmod 400 passwdfile

# Import OpenAM configuration and data
S3_DIR=s3://linz-geodesy-openam/exports/${OPENAM_SERVER_FQDN}
SERVICE_CFG_FILE=service_cfg.xml
DIRECTORY_DATA_FILE=embedded_directory_data.ldif
KEYS_DIRECTORY=keys
IMPORT_DIR=/tmp/openam_imports
OPENAM_BASE_DIR=/usr/share/tomcat8/openam
ADMIN_TOOLS_BASE_DIR=/opt/openam/tools/admin

# Only proceed with import if all files to be imported exist in the S3 bucket (these are a matched set and cannot be
# installed seperately)
SERVICE_CFG_FILE_COUNT=$(aws s3 ls $S3_DIR/ | grep $SERVICE_CFG_FILE | wc -l)
DIRECTORY_DATA_FILE_COUNT=$(aws s3 ls $S3_DIR/ | grep $DIRECTORY_DATA_FILE | wc -l)
KEYS_DIRECTORY_COUNT=$(aws s3 ls $S3_DIR/ | grep $KEYS_DIRECTORY | wc -l)

if [ $SERVICE_CFG_FILE_COUNT -eq 1 ] && [ $DIRECTORY_DATA_FILE_COUNT -eq 1 ] && [ $KEYS_DIRECTORY_COUNT -eq 1 ]; then
  # Prepare local filesystem for downloading export files
  rm -rf $IMPORT_DIR
  mkdir -p $IMPORT_DIR/$KEYS_DIRECTORY

  # Copy file from S3 bucket to local file system
  aws s3 cp $S3_DIR/$SERVICE_CFG_FILE $IMPORT_DIR/$SERVICE_CFG_FILE
  aws s3 cp $S3_DIR/$DIRECTORY_DATA_FILE $IMPORT_DIR/$DIRECTORY_DATA_FILE
  aws s3 cp $S3_DIR/$KEYS_DIRECTORY $IMPORT_DIR/$KEYS_DIRECTORY/. --recursive

  # Replace keystore files
  cp -f $IMPORT_DIR/$KEYS_DIRECTORY/.keypass $OPENAM_BASE_DIR/openam/.
  cp -f $IMPORT_DIR/$KEYS_DIRECTORY/.storepass $OPENAM_BASE_DIR/openam/.
  cp -f $IMPORT_DIR/$KEYS_DIRECTORY/keystore.jks $OPENAM_BASE_DIR/openam/.
  cp -f $IMPORT_DIR/$KEYS_DIRECTORY/keystore.jceks $OPENAM_BASE_DIR/openam/.

  # Import service config file into OpenAM
  echo "y" | $ADMIN_TOOLS_BASE_DIR/openam/bin/ssoadm import-svc-cfg -e geodenc -u amadmin -f $ADMIN_TOOLS_BASE_DIR/passwdfile -X $IMPORT_DIR/$SERVICE_CFG_FILE

  # Restart required, wait until Tomcat startup has finished
  service tomcat8 restart
  until [ "`curl --silent --connect-timeout 1 -i -I http://localhost:8080/openam/ | grep 'HTTP/1.1 200'`" != "" ];
  do
    sleep 10
  done

  # Import directory data file into LDAP directory
  $OPENAM_BASE_DIR/opends/bin/import-ldif\
     --ldifFile $IMPORT_DIR/$DIRECTORY_DATA_FILE\
     --backendID userRoot\
     --hostName ${OPENAM_SERVER_FQDN}\
     --port 4444\
     --bindDN cn=Directory\ Manager\
     --bindPassword ${OPENAM_DS_DIRMGRPASSWD}\
     --trustAll\
     --noPropertiesFile

else
  echo "Some or all import files could not be found for FQDN ${OPENAM_SERVER_FQDN}, nothing imported"
fi

# Generate a JWT signing key, if it doesn't already exist
set +e
sudo keytool -list \
    -alias jwtSigningKey \
    -storetype JCEKS \
    -keystore "${OPENAM_BASE_DIR}/openam/keystore.jceks" \
    -storepass changeit > /dev/null
jwtSigningKeyExists=$?
set -e

if [ $jwtSigningKeyExists != 0 ]; then
    sudo keytool -noprompt -genkeypair \
        -alias jwtSigningKey \
        -keyalg RSA \
        -storetype JCEKS \
        -keystore "${OPENAM_BASE_DIR}/openam/keystore.jceks" \
        -dname "CN=*.gnss.linz.io, OU=[unknown], O=[unknown], L=[unknown], S=[unknown], C=[unknown]" \
        -storepass changeit -keypass changeit
fi

# Ensure a cronjob for exporting the OpenAM backup files exists
CRONTAB=$(crontab -l || :) # crontab -l returns non-zero when crontab is empty
CRONJOB="$(cat <<EOF
#  Export the OpenAM service config
0 1 * * * /opt/openam/tools/admin/scripts/export_openam.sh
EOF
)"

if [[ ! $CRONTAB == *"export_openam"* ]]; then
  (echo "${CRONTAB}"$'\n'"${CRONJOB}") | crontab -
fi

