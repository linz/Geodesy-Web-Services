#!/bin/bash
export AWS_PROFILE=geodesy
PATH=~/.local/bin:$PATH
mvn package
ENV=A GEODESY_WEB_SERVICES_VERSION=1.0.0-SNAPSHOT make clean stack
