#!/usr/bin/env bash

set -e

. ${BASH_SOURCE%/*}/env.sh

cd ${BASH_SOURCE%/*}

if [[ ${ENV,,} == "dev" ]]; then
    # Run system tests
    java \
        -DwebServicesUrl=http://localhost:8080 \
        -DoauthProviderUrl=${DEV_OAUTH_ENDPOINT} \
        -jar gws-system-test.jar \
        -testjar gws-system-test.jar
fi
