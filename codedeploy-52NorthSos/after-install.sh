#!/usr/bin/env bash

. ${BASH_SOURCE%/*}/env.sh

DATASOURCE_PROPERTIES=/usr/share/tomcat8/webapps/52n-sos-webapp##4.3.7/WEB-INF/datasource.properties

# set database login paramaters
sed -i 's/${geodesy-db-url}/jdbc:postgresql:\/\/'"${RDS_ENDPOINT}\/GeodesyDb/" $DATASOURCE_PROPERTIES
sed -i 's/${geodesy-db-username}/'"${DB_USERNAME}/" $DATASOURCE_PROPERTIES
sed -i 's/${geodesy-db-password}/'"${DB_PASSWORD}/" $DATASOURCE_PROPERTIES

# ensure all files in the application are owned by tomcat
chown -R tomcat:tomcat /usr/share/tomcat8/webapps/

# Start tomcat
service tomcat8 start
