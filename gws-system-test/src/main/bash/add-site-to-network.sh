#!/usr/bin/env bash

set -e

gws=http://localhost:8081
openam=http://localhost:8083/openam

clientId=GnssSiteManager
clientPassword=

username=
password=

jwt=$(curl -s --user ${clientId}:${clientPassword} --data "grant_type=password&username=${username}&password=${password}&scope=openid profile" ${openam}/oauth2/access_token?realm=/ | jq .id_token | tr -d '"')

function addSiteToNetwork {
    addToNetwork=$(curl -s ${gws}/corsSites?fourCharacterId=${1} | jq ._embedded.corsSites[0]._links.addToNetwork.href | tr -d '"')
    networkId=$(curl -s ${gws}/corsNetworks?name=${2} | jq ._embedded.corsNetworks[0].id)

    if [ "${networkId}" = "null" ]; then
        echo "Network not found"
        return
    fi

    curl -X PUT "${addToNetwork}?networkId=${networkId}" \
        -H "Authorization: Bearer ${jwt}"
}

addSiteToNetwork $1 $2
