#!/usr/bin/env bash

# Stop Tomcat
service tomcat8 stop

rm /usr/share/tomcat8/webapps/52n-sos-webapp##4.3.7/configuration.db
