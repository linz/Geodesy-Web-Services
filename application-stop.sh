#!/usr/bin/env bash
# Stop Tomcat
service tomcat8 stop
# Undeploy Geodesy Web Services
rm -rf /usr/share/tomcat8/webapps/ROOT*
