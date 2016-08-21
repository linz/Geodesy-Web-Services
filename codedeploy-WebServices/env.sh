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

# TODO: Why is the default region not Sydney? How do we pull in the correct region?
CREDSTASH="/usr/local/bin/credstash -r ap-southeast-2"
DB_USERNAME_KEY="${ENV^}"GeodesyDbUsername
DB_PASSWORD_KEY="${ENV^}"GeodesyDbPassword
DB_USERNAME=$(${CREDSTASH} get ${DB_USERNAME_KEY})
DB_PASSWORD=$(${CREDSTASH} get ${DB_PASSWORD_KEY})

unset DEPLOYMENT_GROUP_NAME_CUT
unset CREDSTASH

