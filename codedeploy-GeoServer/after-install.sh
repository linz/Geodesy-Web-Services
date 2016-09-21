#!/usr/bin/env bash

. ${BASH_SOURCE%/*}/env.sh

CONTEXT_DESCRIPTOR=/usr/share/tomcat8/conf/Catalina/localhost/geoserver.xml

# set database login paramaters
sed -i 's/${geodesy-db-url}/jdbc:postgresql:\/\/'"${RDS_ENDPOINT}\/GeodesyDb/" $CONTEXT_DESCRIPTOR
sed -i 's/${geodesy-db-username}/'"${DB_USERNAME}/" $CONTEXT_DESCRIPTOR
sed -i 's/${geodesy-db-password}/'"${DB_PASSWORD}/" $CONTEXT_DESCRIPTOR

# ensure all files in the GeoServer application are owned by tomcat
chown -R tomcat:tomcat /usr/share/tomcat8/webapps/geoserver
