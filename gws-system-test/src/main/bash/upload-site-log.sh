#!/usr/bin/env bash
set -e

gws=http://localhost:8081
#openam=http://localhost:8083/openam

#gws=https://devgeodesy-webservices.gnss.linz.io

# openam=https://prodgeodesy-openam.geodesy.ga.gov.au/openam

export AWS_PROFILE=geodesy

clientId=GnssSiteManager
clientPassword=

username=
password=

jwt=$(aws cognito-idp initiate-auth --client-id 4hpg5n34kduiedr7taqipdo77h --auth-flow USER_PASSWORD_AUTH --auth-parameters USERNAME=mbro,PASSWORD="jtest\,123"| jq '.[] |.IdToken'| tr -d '"')

echo $jwt

curl -v  --data-binary @${1} ${gws}/siteLogs/upload \
    -H "Authorization: Bearer ${jwt}"
