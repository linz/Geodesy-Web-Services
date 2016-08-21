#!/usr/bin/env bash
# Unpack WAR file
mkdir /usr/share/tomcat8/webapps/ROOT
unzip ROOT.war -d /usr/share/tomcat8/webapps/ROOT

# Obtain Env
DEPLOYMENT_GROUP_NAME_CUT=$(echo "${DEPLOYMENT_GROUP_NAME}" | cut -c1-3)

if [ "${DEPLOYMENT_GROUP_NAME_CUT,,}" == "dev" ]
then
    ENV="$DEPLOYMENT_GROUP_NAME_CUT"
else
    ENV=$(echo $DEPLOYMENT_GROUP_NAME | cut -c1-4)
fi

# Set database endpoint
RDS_ENDPOINT="${ENV^}"geodesyrds.geodesy.ga.gov.au
sed -i 's/${geodesy-db-url}/jdbc:postgresql:\/\/'"${RDS_ENDPOINT}\/geodesy/" /usr/share/tomcat8/webapps/ROOT/META-INF/context.xml

# Set database login
DB_USERNAME_KEY="${ENV^}"GeodesyDbUsername
DB_PASSWORD_KEY="${ENV^}"GeodesyDbPassword
DB_USERNAME=$(credstash get ${DB_USERNAME_KEY})
DB_PASSWORD=$(credstash get ${DB_PASSWORD_KEY})
sed -i 's/${geodesy-db-username}/'"${DB_USERNAME}/" /usr/share/tomcat8/webapps/ROOT/META-INF/context.xml
sed -i 's/${geodesy-db-password}/'"${DB_PASSWORD}/" /usr/share/tomcat8/webapps/ROOT/META-INF/context.xml

