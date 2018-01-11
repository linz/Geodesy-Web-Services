#!/usr/bin/env bash

set -e

ENV=$1

cd ${BASH_SOURCE%/*}/..

./codedeploy-push-deploy.sh \
    --application Geodesy \
    --env ${ENV} \
    --deployment_config_name CodeDeployDefault.OneAtATime \
    --s3_bucket linz-geodesy-codedeploy \
    --artefact_ext zip \
    --unit WebServices
