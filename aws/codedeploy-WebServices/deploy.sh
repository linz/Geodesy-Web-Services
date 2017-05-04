#!/usr/bin/env bash

ENV=$1

cd ${BASH_SOURCE%/*}/..

cp ../gws-webapp/target/geodesy-web-services.war ../gws-system-test/target/gws-system-test.jar codedeploy-WebServices/

./codedeploy-push-deploy.sh \
    --application Geodesy \
    --env ${ENV} \
    --deployment_config_name CodeDeployDefault.OneAtATime \
    --s3_bucket geodesy-codedeploy \
    --artefact_ext zip \
    --unit WebServices
