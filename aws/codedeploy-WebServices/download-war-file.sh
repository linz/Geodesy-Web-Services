#!/usr/bin/env bash

mvn="mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true"

${mvn} package
mv gws-webapp-*.war geodesy-web-services.war

