#!/usr/bin/env bash

# Stop Tomcat if it is running
TOMCAT_STATUS=$(eval "service tomcat8 status")
if [[ $TOMCAT_STATUS == *"running"* ]]; then
  service tomcat8 stop
fi

# rm /usr/share/tomcat8/webapps/52n-sos-webapp##4.3.7/configuration.db
