#!/usr/bin/env bash

outputDir=$1
gws=https://dev.geodesy.ga.gov.au

fourCharIds=$(curl -s "${gws}/siteLogs?size=2000" | jq ._embedded.siteLogs[].siteIdentification.fourCharacterId | tr -d "'" | xargs)

for fourCharId in ${fourCharIds}; do
    echo ${fourCharId}
    curl -s "${gws}/siteLogs/search/findByFourCharacterId?id=${fourCharId}&format=geodesyml" > "${outputDir}/${fourCharId}.xml"
done



