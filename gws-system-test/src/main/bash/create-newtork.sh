#!/usr/bin/env bash

set -e

gws=http://localhost:8081
openam=http://localhost:8083/openam

# gws=https://gws.geodesy.ga.gov.au
# openam=https://prodgeodesy-openam.geodesy.ga.gov.au/openam

clientId=GnssSiteManager
clientPassword=

username=
password=

jwt=$(curl -s --user ${clientId}:${clientPassword} --data "grant_type=password&username=${username}&password=${password}&scope=openid profile" ${openam}/oauth2/access_token?realm=/ | jq .id_token | tr -d '"')

function createNetwork {
    name=${1}
    description=${2}
    curl -s ${gws}/corsNetworks -d "{\"name\": \"${name}\", \"description\": \"${description}\"}" \
        -H "Authorization: Bearer ${jwt}" \
        -H "Content-type: application/json"
}

createNetwork "$@"
