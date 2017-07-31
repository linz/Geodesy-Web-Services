#!/usr/bin/env bash

outputDir=$1
# gws=http://localhost:8081
gws=https://test.geodesy.ga.gov.au

fourCharIds=$(curl -s "${gws}/corsSites?size=2000" | jq ._embedded.corsSites[].fourCharacterId | tr -d '"' | xargs)

for fourCharId in ${fourCharIds}; do
    echo ${fourCharId}
    curl -s "${gws}/siteLogs/search/findByFourCharacterId?id=${fourCharId}&format=geodesyml" > "${outputDir}/${fourCharId}.xml"
done



