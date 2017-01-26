#!/usr/bin/env bash

set -e

. /env.sh

mkdir -p "${TOMCAT_HOME}"/webapps/
if [ -d /webapps ]; then
    rsync --ignore-missing-args -ur /webapps/ "${TOMCAT_HOME}"/webapps/
fi

startup.sh
wait_for_tomcat
java -jar /opt/openam/tools/config/openam-configurator-tool-13.5.0.jar -f /tmp/openam/openam.cfg
shutdown.sh
