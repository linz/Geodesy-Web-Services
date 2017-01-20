#!/usr/bin/env bash

BASE_DIR=/opt/openam/tools/admin
EXPORT_DIR=/tmp
OUTPUT_FILE=${OPENAM_SERVER_FQDN}_servicecfg.xml
S3_BUCKET=s3://geodesy-openam/service-config-export

# Export OpenAM Service Configuration to xml file
$BASE_DIR/openam/bin/ssoadm export-svc-cfg -e geodenc -u amadmin -f $BASE_DIR/passwdfile -o $EXPORT_DIR/$OUTPUT_FILE

# Write export file to S3 bucket
aws s3 cp $EXPORT_DIR/$OUTPUT_FILE $S3_BUCKET/$OUTPUT_FILE
