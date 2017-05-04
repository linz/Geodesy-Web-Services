#!/usr/bin/env bash

set -e

. ${BASH_SOURCE%/*}/env.sh

cd ${BASH_SOURCE%/*}

# Run system tests
java -jar gws-system-test.jar \
    -testjar gws-system-test.jar \
    -DwebServicesUrl=http://localhost:8080
    -DoauthProviderUrl=https://${ENV}geodesy-openam.geodesy.ga.gov.au/openam/oauth2
