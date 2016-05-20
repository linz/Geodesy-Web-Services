#!/bin/bash
export AWS_PROFILE=geodesy
PATH=~/.local/bin:$PATH
ENV=D DB_ID=geodesy-dev GEODESY_WEB_SERVICES_VERSION=1.0.0-SNAPSHOT make clean stack
