#!/usr/bin/env bash

# Stop Tomcat
service tomcat8 stop

# Remove existing OpenAM configuration
rm -rf /usr/share/tomcat8/openam

# Start Tomcat 
service tomcat8 start

# Wait until Tomcat startup has finished
until [ "`curl --silent --connect-timeout 1 -I http://localhost:8080 | grep 'Coyote'`" != "" ];
do
  sleep 10
done
