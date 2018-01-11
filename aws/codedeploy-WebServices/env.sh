#!/usr/bin/env bash

# Obtain Env
DEPLOYMENT_GROUP_NAME_CUT=$(echo "${DEPLOYMENT_GROUP_NAME}" | cut -c1-3)

if [ "${DEPLOYMENT_GROUP_NAME_CUT,,}" == "dev" ]
then
    ENV=${DEPLOYMENT_GROUP_NAME_CUT}
else
    ENV=$(echo $DEPLOYMENT_GROUP_NAME | cut -c1-4)
fi

# Get RegionID
EC2_AVAIL_ZONE=`curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone`
AWS_DEFAULT_REGION="`echo \"$EC2_AVAIL_ZONE\" | sed -e 's:\([0-9][0-9]*\)[a-z]*\$:\\1:'`"

AWS="aws --region ${AWS_DEFAULT_REGION}"

DB_NAME=GeodesyDb
RDS_INSTANCE_ID=${ENV,,}${DB_NAME,,}
RDS_ENDPOINT=$(${AWS} rds describe-db-instances --db-instance-identifier ${RDS_INSTANCE_ID} | grep Address | awk -F'"' {'print $4'})

OPENAM_ENDPOINT=https://${ENV,,}geodesy-openam.fsitelog.ga/openam

CREDSTASH="/usr/local/bin/credstash -r ${AWS_DEFAULT_REGION}"

RDS_MASTER_USERNAME_KEY=${ENV^}GeodesyDbMasterUsername
RDS_MASTER_PASSWORD_KEY=${ENV^}GeodesyDbMasterPassword
RDS_MASTER_USERNAME=$(${CREDSTASH} get ${RDS_MASTER_USERNAME_KEY})
RDS_MASTER_PASSWORD=$(${CREDSTASH} get ${RDS_MASTER_PASSWORD_KEY})

DB_USERNAME_KEY="${ENV^}"GeodesyDbUsername
DB_PASSWORD_KEY="${ENV^}"GeodesyDbPassword
DB_USERNAME=$(${CREDSTASH} get ${DB_USERNAME_KEY})
DB_PASSWORD=$(${CREDSTASH} get ${DB_PASSWORD_KEY})

unset DEPLOYMENT_GROUP_NAME_CUT
unset CREDSTASH
