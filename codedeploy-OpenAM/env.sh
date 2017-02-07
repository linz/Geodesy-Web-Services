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

CREDSTASH="/usr/local/bin/credstash -r ${AWS_DEFAULT_REGION}"

OPENAM_ADMIN_PWD_KEY=${ENV^}GeodesyOpenAmAdminPassword
OPENAM_ADMIN_PWD=$(${CREDSTASH} get ${OPENAM_ADMIN_PWD_KEY})
OPENAM_AMLDAPUSERPASSWD_KEY=${ENV^}GeodesyOpenAmLdapUserPassword
OPENAM_AMLDAPUSERPASSWD=$(${CREDSTASH} get ${OPENAM_AMLDAPUSERPASSWD_KEY})
OPENAM_DS_DIRMGRPASSWD_KEY=${ENV^}GeodesyOpenAmDsDirMgrPassword
OPENAM_DS_DIRMGRPASSWD=$(${CREDSTASH} get ${OPENAM_DS_DIRMGRPASSWD_KEY})

OPENAM_SERVER_FQDN=${ENV,,}geodesy-openam.geodesy.ga.gov.au

unset CREDSTASH
