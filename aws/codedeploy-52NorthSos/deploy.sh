#!/usr/bin/env bash

ENV=$1

# Download 52n-sensorweb-sos zipped software package from S3
#aws --no-sign-request s3 cp s3://geodesy-web-services/52north/52n-sensorweb-sos-4.3.7.zip .

cd ${BASH_SOURCE%/*}

(cd ..; ./codedeploy-push-deploy.sh \
    --application Geodesy \
    --env ${ENV} \
    --deployment_config_name CodeDeployDefault.OneAtATime \
    --s3_bucket geodesy-codedeploy \
    --artefact_ext zip \
    --unit 52NorthSos
)
