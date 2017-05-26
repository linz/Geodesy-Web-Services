#!/usr/bin/env bash

set -e

. ${BASH_SOURCE%/*}/env.sh

cd ${BASH_SOURCE%/*}

if [[ ${ENV,,} == "dev" ]]; then
    # Run system tests
    java \
        -DwebServicesUrl=http://localhost:8080 \
        -DoauthProviderUrl=https://${ENV}geodesy-openam.geodesy.ga.gov.au/openam/oauth2 \
        -jar gws-system-test.jar \
        -testjar gws-system-test.jar
fi
