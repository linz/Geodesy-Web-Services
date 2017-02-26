#!/usr/bin/env bash

set -e

. /env.sh
. /env-openam.sh

# Deploy OpenAM
mkdir -p "${TOMCAT_HOME}"/webapps/
if [ -d /webapps ]; then
    rsync --ignore-missing-args -ur /webapps/ "${TOMCAT_HOME}"/webapps/
fi

startup.sh
wait_for_tomcat

# Perform initial configiration
java -jar $ADMIN_TOOLS_DIR/config/openam-configurator-tool-13.5.0.jar -f $OPENAM_CFG_FILE

(cd $ADMIN_TOOLS_DIR/admin && ./setup -p $OPENAM_BASE_DIR -d $ADMIN_TOOLS_DIR/admin/debug -l $ADMIN_TOOLS_DIR/admin/log --acceptLicense)

# Import service config file into OpenAM
echo "$ADMIN_PWD" > $ADMIN_TOOLS_DIR/admin/passwdfile
echo "y" | $SSOADM import-svc-cfg -e geodenc -u amadmin -f $ADMIN_TOOLS_DIR/admin/passwdfile -X $SERVICE_CFG_FILE

# Replace keystore files
cp -f $KEYS_DIR/keystore.jks $OPENAM_BASE_DIR/openam/.
rm $OPENAM_BASE_DIR/openam/keystore.jceks
cp -f $KEYS_DIR/.keypass $OPENAM_BASE_DIR/openam/.
cp -f $KEYS_DIR/.storepass $OPENAM_BASE_DIR/openam/.

# Export self-signed signing key public certificate
keytool -exportcert -alias signingKey -file idfSelfSignedCert.crt \
    -keystore "$KEYS_DIR"/keystore.jks -storepass G2dioga1

# Import self-signed signing key public certificate into the JVM keystore
keytool -importcert -noprompt -alias signingKey -file idfSelfSignedCert.crt -trustcacerts \
    -keystore "$JAVA_HOME"/lib/security/cacerts \
    -storepass changeit

# Import directory data file into LDAP directory
$IMPORT_LDIF\
 --ldifFile $DIRECTORY_DATA_FILE\
 --backendID userRoot\
 --hostName localhost\
 --port 4444\
 --bindDN cn=Directory\ Manager\
 --bindPassword $DS_DIRMGRPASSWD\
 --trustAll\
 --noPropertiesFile

shutdown.sh

cp "$CONFIG_DIR/web.xml" "$TOMCAT_HOME/webapps/openam/WEB-INF/"
