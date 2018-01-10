#!/usr/bin/env bash

S3_BASE_DIR=s3://linz-geodesy-openam/exports
FQDN=${OPENAM_SERVER_FQDN}
SERVICE_CFG_FILE=service_cfg.xml
DIRECTORY_DATA_FILE=embedded_directory_data.ldif
KEYS_DIRECTORY=keys
EXPORT_DIR=/tmp/openam_exports
SERVICE_CFG_FILE=service_cfg.xml
ADMIN_TOOLS_BASE_DIR=/opt/openam/tools/admin
CONFIG_TOOLS_BASE_DIR=/opt/openam/tools/config
OPENAM_BASE_DIR=/usr/share/tomcat8/openam

# Prepare local filesystem for export files
rm -rf $EXPORT_DIR
mkdir -p $EXPORT_DIR/$KEYS_DIRECTORY
chmod -R 777 $EXPORT_DIR


# Export OpenAM Service Configuration to xml file
$ADMIN_TOOLS_BASE_DIR/openam/bin/ssoadm export-svc-cfg -e geodenc -u amadmin -f $ADMIN_TOOLS_BASE_DIR/passwdfile -o $EXPORT_DIR/$SERVICE_CFG_FILE

# Write exported Service Configuration file to S3 bucket
aws s3 cp $EXPORT_DIR/$SERVICE_CFG_FILE $S3_BASE_DIR/$FQDN/$SERVICE_CFG_FILE


# Export embedded LDAP data directory to LDIF file
$OPENAM_BASE_DIR/opends/bin/export-ldif\
 --ldifFile $EXPORT_DIR/$DIRECTORY_DATA_FILE\
 --backendID userRoot\
 --appendToLDIF\
 --hostName $FQDN\
 --port 4444\
 --bindDN cn=Directory\ Manager\
 --bindPassword ${OPENAM_DS_DIRMGRPASSWD}\
 --trustAll\
 --noPropertiesFile

# Write exported LDAP data directory file to S3 bucket
aws s3 cp $EXPORT_DIR/$DIRECTORY_DATA_FILE $S3_BASE_DIR/$FQDN/$DIRECTORY_DATA_FILE


# Write the exported keystore files to S3 bucket
cp $OPENAM_BASE_DIR/openam/.keypass $EXPORT_DIR/$KEYS_DIRECTORY/.
cp $OPENAM_BASE_DIR/openam/keystore.jceks $EXPORT_DIR/$KEYS_DIRECTORY/.
cp $OPENAM_BASE_DIR/openam/keystore.jks $EXPORT_DIR/$KEYS_DIRECTORY/.
cp $OPENAM_BASE_DIR/openam/.storepass $EXPORT_DIR/$KEYS_DIRECTORY/.

aws s3 cp $EXPORT_DIR/$KEYS_DIRECTORY $S3_BASE_DIR/$FQDN/$KEYS_DIRECTORY/ --recursive
