#!/usr/bin/env bash

# Obtain Env
DEPLOYMENT_GROUP_NAME_CUT=$(echo "${DEPLOYMENT_GROUP_NAME}" | cut -c1-3)

if [ "${DEPLOYMENT_GROUP_NAME_CUT,,}" == "dev" ]
then
    ENV=${DEPLOYMENT_GROUP_NAME_CUT}
else
    ENV=$(echo $DEPLOYMENT_GROUP_NAME | cut -c1-4)
fi

# set database endpoint
# RDS_ENDPOINT="${ENV^}"geodesyrds.geodesy.ga.gov.au
# temporarily hard-coded to the dev database
RDS_ENDPOINT=dd1iyix40zjic7t.cxm7lrsl3bbf.ap-southeast-2.rds.amazonaws.com

# Get RegionID
EC2_AVAIL_ZONE=`curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone`
AWS_DEFAULT_REGION="`echo \"$EC2_AVAIL_ZONE\" | sed -e 's:\([0-9][0-9]*\)[a-z]*\$:\\1:'`"

# Credstash
CREDSTASH="/usr/local/bin/credstash -r ${AWS_DEFAULT_REGION}"
DB_USERNAME_KEY="${ENV^}"GeodesyDbUsername
DB_PASSWORD_KEY="${ENV^}"GeodesyDbPassword
DB_USERNAME=$(${CREDSTASH} get ${DB_USERNAME_KEY})
DB_PASSWORD=$(${CREDSTASH} get ${DB_PASSWORD_KEY})

unset DEPLOYMENT_GROUP_NAME_CUT
unset CREDSTASH

