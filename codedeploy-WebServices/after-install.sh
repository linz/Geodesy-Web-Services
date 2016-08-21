#!/usr/bin/env bash

. ${BASH_SOURCE%/*}/env.sh

# TODO: Why is the default region not Sydney? How do we pull in the correct region?
CREDSTASH="/usr/local/bin/credstash -r ap-southeast-2"

cd /usr/share/tomcat8/webapps

# deploy new application
rm -rf ROOT*
mv geodesy-web-services.war ROOT.war
unzip ROOT.war -d ROOT

# set database endpoint
# RDS_ENDPOINT="${ENV^}"geodesyrds.geodesy.ga.gov.au
# temporarily hard-coded to the dev database
RDS_ENDPOINT=dd1iyix40zjic7t.cxm7lrsl3bbf.ap-southeast-2.rds.amazonaws.com
sed -i 's/${geodesy-db-url}/jdbc:postgresql:\/\/'"${RDS_ENDPOINT}\/GeodesyDb/" ROOT/META-INF/context.xml

# set database login
DB_USERNAME_KEY="${ENV^}"GeodesyDbUsername
DB_PASSWORD_KEY="${ENV^}"GeodesyDbPassword
DB_USERNAME=$(${CREDSTASH} get ${DB_USERNAME_KEY})
DB_PASSWORD=$(${CREDSTASH} get ${DB_PASSWORD_KEY})
sed -i 's/${geodesy-db-username}/'"${DB_USERNAME}/" ROOT/META-INF/context.xml
sed -i 's/${geodesy-db-password}/'"${DB_PASSWORD}/" ROOT/META-INF/context.xml

