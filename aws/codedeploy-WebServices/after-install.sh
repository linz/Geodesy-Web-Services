#!/usr/bin/env bash

. ${BASH_SOURCE%/*}/env.sh

cd /usr/share/tomcat8/webapps

# deploy new application
rm -rf ROOT*
mv geodesy-web-services.war ROOT.war
unzip ROOT.war -d ROOT

sed -i 's/${geodesy-db-url}/jdbc:postgresql:\/\/'"${RDS_ENDPOINT}\/GeodesyDb/" ROOT/META-INF/context.xml

# set database login
sed -i 's/${geodesy-db-username}/'"${DB_USERNAME}/" ROOT/META-INF/context.xml
sed -i 's/${geodesy-db-password}/'"${DB_PASSWORD}/" ROOT/META-INF/context.xml

sed -i s,'${oauthProviderUrl}',"${OPENAM_ENDPOINT}"/oauth2, ROOT/WEB-INF/classes/config.properties
