#!/usr/bin/env bash

# Stop Tomcat
service tomcat8 stop

# remove the current GeoServer data directory
rm -r /usr/share/tomcat8/webapps/geoserver/data
