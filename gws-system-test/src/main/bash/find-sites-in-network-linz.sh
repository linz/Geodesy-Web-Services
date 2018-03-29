#!/usr/bin/env bash

set -e

#gws=http://localhost:8081
#openam=http://localhost:8083/openam

gws=https://devgeodesy-webservices.gnss.linz.io


function knownNetworks {
    curl -s ${gws}/corsNetworks | jq ._embedded.corsNetworks[].name | xargs echo
}

function printUsage {
    echo "Output metedata links for all sites in a given CORS network."
    echo "Usage: $(basename "${0}") <network-name>"
    echo "  where network-name is one of following: $(knownNetworks)"
}

if [[ $# != 1 ]]; then
    printUsage
    exit 1
fi

networkName=${1}
networkId=$(curl -s "${gws}/corsNetworks?name=${networkName}" | jq ._embedded.corsNetworks[0].id)

if [ "${networkId}" = "null" ]; then
    echo "No such network, please choose one of following: $(knownNetworks)"
    exit 1
fi

fourCharIds=$(curl -s "${gws}/corsSites?networkTenancies.corsNetworkId=${networkId}&size=1000" | jq ._embedded.corsSites[].fourCharacterId | tr -d '"')

for fourCharId in ${fourCharIds}; do
    echo "${siteManager}/siteLog/${fourCharId}"
done

