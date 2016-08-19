#!/usr/bin/env bash

#Small script designed to:
# Push an local directory of the application repository to s3 as an tar.gz
# Deploy a application artefact
# sh ./$infra/configuration_code/scripts/app_deploy/codedeploy_push2s3_deploy.sh \
# -a $app -e $env -c $deployment_config_name -b $s3_bucket

## Assign arguments to variables
while [ $# -ge 1 ]
do
    key=$1
    case $key in
        -a|--application)
        app=$2 && echo !! app=${app}
        shift
        ;;
        -e|--env)
        env=$2 && echo !! env=${env}
        shift
        ;;
        -c|--deployment_config_name)
        deployment_config_name=$2 && echo !! deployment_config_name=${deployment_config_name}
        shift
        ;;
        -b|--s3_bucket)
        s3_bucket=$2 && echo !! s3_bucket=${s3_bucket}
        shift
        ;;
        -t|--bundle_type)
        bundle_type=$2 && echo !! bundle_type=${bundle_type}
        shift
        ;;
        -x|--artefact_ext)
        artefact_ext=$2 && echo !! artefact_ext=${artefact_ext}
        shift
        ;;
        *)
        echo "Unknown option ${1}"
	echo "Usage: sh ./$infra/configuration_code/scripts/app_deploy/codedeploy_push2s3_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket"
        exit 1
	;;
    esac
    shift
done

## Check variables
if [ -z ${app} ] ; then
    echo "Error: Application name has not been set"
    echo "Usage: sh ./$infra/configuration_code/scripts/app_deploy/codedeploy_push2s3_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket"
fi

if [ -z ${env} ] ; then
    echo "Error: Environment has not been set"
    echo "Usage: sh ./$infra/configuration_code/scripts/app_deploy/codedeploy_push2s3_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket"
fi

if [ -z ${deployment_config_name} ] ; then
    echo "Error: Deployment Config has not been set"
    echo "Usage: sh ./$infra/configuration_code/scripts/app_deploy/codedeploy_push2s3_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket"
fi

if [ -z ${s3_bucket} ] ; then
    echo "Error: S3 bucket name has not been set"
    echo "Usage: sh ./$infra/configuration_code/scripts/app_deploy/codedeploy_push2s3_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket"
fi

if [ -z ${bundle_type} ] ; then
    echo "Error: Code Deploy bundle type has not been set"
    echo "Usage: sh ./$infra/configuration_code/scripts/app_deploy/codedeploy_push2s3_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket"
fi

if [ -z ${artefact_ext} ] ; then
    echo "Error: Artefact extension for key in deployment not found e.g. tar.gz"
    echo "Usage: sh ./$infra/configuration_code/scripts/app_deploy/codedeploy_push2s3_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket"
fi


# Push Application
push \
--application-name ${env^}Geodesy \
--s3-location s3://${s3_bucket}/GeodesyCD/


## Declare variables
date=$(date +%d_%m_%y) && echo !! date=${date}
time=$(date +%H_%M_%S) && echo !! time=${time}
deployment_group_name=${env} && echo !! deployment_group_name=${deployment_group_name}
description_create_deployment="${deployment_group_name}-${date}-${time}" && echo !! description_create_deployment=${description_create_deployment}
key=${app}/${app}.${artefact_ext} && echo !! key=${key}

## Code Deploy Application from output

create_deployment="$(aws deploy create-deployment --application-name ${app} --deployment-config-name ${deployment_config_name} --deployment-group-name ${deployment_group_name} --description $description_create_deployment --s3-location bucket=${s3_bucket},bundleType=${bundle_type},key=${key})"
echo !! create_deployment=${create_deployment}

# TEST
# This is a small script to check to see if a code deploy deployment has finished creating in AWS

## Variables
deployment_id="$(echo ${create_deployment} | python -c "
import sys
import json
id = json.loads(''.join(sys.stdin.readlines()))
print id['deploymentId']
"
)"

##Validate Stack is created
deploy_status="InProgress"

while [ 1 ]; do
    response=$(aws deploy get-deployment --deployment-id ${deployment_id} 2>&1)
    responseOrig="${response}"
    response=$(echo "${response}" | tr '\n' ' ' | tr -s " " | sed -e 's/^ *//' -e 's/ *$//')

    if echo "${response}" | egrep -q "status"
    then
        echo "Response contains Code Deploy Deployment status"
    else
        echo "Error occurred finding Code Deploy deployment. Error:"
        echo "${responseOrig}"
        exit 1
    fi

    deploy_status=$(echo ${response} | sed -e 's/^.*"status"[ ]*:[ ]*"//' -e 's/".*//')
    echo "Status: ${deploy_status}"

    if [ "${deploy_status}" = "Failed" ] || [ "${deploy_status}" = "Stopped" ]; then
        echo "Error occurred deploying Code Deployment Deployment-ID: ${deployment_id}. Details:"
        echo "${responseOrig}"
        exit 1
    elif [ "${deploy_status}" = "Succeeded" ]; then
        break
    fi

    # Sleep for 5 seconds, if stack creation in progress
    sleep 5
done

