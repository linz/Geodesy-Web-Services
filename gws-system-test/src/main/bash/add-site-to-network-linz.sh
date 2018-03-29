#!/usr/bin/env bash

set -e

#gws=http://localhost:8081
#openam=http://localhost:8083/openam

gws=https://devgeodesy-webservices.gnss.linz.io

# openam=https://prodgeodesy-openam.geodesy.ga.gov.au/openam

export AWS_PROFILE=geodesy

clientId=GnssSiteManager
clientPassword=

username=
password=

jwt=$(aws cognito-idp initiate-auth --client-id 4hpg5n34kduiedr7taqipdo77h --auth-flow USER_PASSWORD_AUTH --auth-parameters USERNAME=mbro,PASSWORD="jtest\,123"| jq '.[] |.IdToken'| tr -d '"')


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
