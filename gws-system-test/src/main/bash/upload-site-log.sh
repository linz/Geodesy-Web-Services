#!/usr/bin/env bash

gws=https://devgeodesy-webservices.fsitelog.ga/
openam=https://devgeodesy-openam.fsitelog.ga/openam

clientId=GnssSiteManager
clientPassword=

username=user.x
password=gumby123X

jwt=$(curl -s --user ${clientId}:${clientPassword} --data "grant_type=password&username=${username}&password=${password}&scope=openid profile" ${openam}/oauth2/access_token?realm=/ | jq .id_token | tr -d '"')

curl -v --data-binary @${1} ${gws}/siteLogs/upload \
    -H "Authorization: Bearer ${jwt}"
