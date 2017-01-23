#!/usr/bin/env bash

BASE_DIR=/usr/share/tomcat8/openam/opends/bin
EXPORT_DIR=/tmp
OUTPUT_FILE=${OPENAM_SERVER_FQDN}_embedded_directory.ldif
S3_BUCKET=s3://geodesy-openam/directory-data-export

# Export embedded LDAP data directory to LDIF file
$BASE_DIR/export-ldif\
 --ldifFile $EXPORT_DIR/$OUTPUT_FILE\
 --backendID userRoot\
 --appendToLDIF\
 --hostName ${OPENAM_SERVER_FQDN}
 --port 4444\
 --bindDN cn=Directory\ Manager\
 --bindPassword ${OPENAM_DS_DIRMGRPASSWD}\
 --trustAll\
 --noPropertiesFile

# Write export file to S3 bucket
aws s3 cp $EXPORT_DIR/$OUTPUT_FILE $S3_BUCKET/$OUTPUT_FILE
