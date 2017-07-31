#!/usr/bin/env bash

gws=http://localhost:8081
openam=http://localhost:8083/openam

clientId=GnssSiteManager
clientPassword=

username=
password=

jwt=$(curl -s --user ${clientId}:${clientPassword} --data "grant_type=password&username=${username}&password=${password}&scope=openid profile" ${openam}/oauth2/access_token?realm=/ | jq .id_token | tr -d '"')

curl --data-binary @${1} ${gws}/siteLogs/upload \
    -H "Authorization: Bearer ${jwt}"
