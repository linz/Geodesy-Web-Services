#!/usr/bin/env bash

set -e

java_link="$(which java)"
java=$(readlink -f "${java_link}")
java_bin=$(dirname "${java}")
JAVA_HOME=$(dirname "${java_bin}")
export JAVA_HOME

catalina_link="$(which catalina.sh)"
catalina=$(readlink -f "${catalina_link}")
tomcat_bin=$(dirname "${catalina}")
TOMCAT_HOME=$(dirname "${tomcat_bin}")
export TOMCAT_HOME

mkdir -p "${TOMCAT_HOME}"/webapps/
if [ -d /webapps ]; then
    rsync --ignore-missing-args -ur /webapps/ "${TOMCAT_HOME}"/webapps/
fi

exec "$@"
