#!/usr/bin/env bash

ENV=$1

cd ${BASH_SOURCE%/*}

./download-war-file.sh

(cd ..; ./codedeploy-push-deploy.sh \
    --application Geodesy \
    --env ${ENV} \
    --deployment_config_name CodeDeployDefault.OneAtATime \
    --s3_bucket geodesy-codedeploy \
    --artefact_ext zip \
    --unit WebServices
)
