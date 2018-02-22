#!/usr/bin/env bash
#Small script designed to:
# Push an local directory of the application repository to s3 as an tar.gz
# Deploy a application artefact

set -e

# Reset
Color_Off="\033[0m"      # Text Reset

# Regular Colors
Black='\033[0;30m'        # Black
Red='\033[0;31m'          # Red
Green='\033[0;32m'        # Green
Yellow='\033[0;33m'       # Yellow
Blue='\033[0;34m'         # Blue
Purple='\033[0;35m'       # Purple
Cyan='\033[0;36m'         # Cyan
White='\033[0;37m'        # White

# Assign arguments to variables
while [ $# -ge 1 ]
do
    key=$1
    case $key in
        -a|--application)
        app="LI-Geodesy" && echo -e !! "${Red}"  app= "${app}" "$Color_Off" 
        shift
        ;;
        -e|--env)
        env="${2^}" && echo -e !! "${Blue}"  env=$env "$Color_Off"
        shift
        ;;
        -c|--deployment_config_name)
        deployment_config_name=$2 && echo  -e !! "${Yellow}" deployment_config_name=${deployment_config_name} "$Color_Off"
        shift
        ;;
        -b|--s3_bucket)
        s3_bucket=$2 && echo -e !! "${Purple}" s3_bucket=${s3_bucket} "$Color_Off"
        shift
        ;;
        -u|--unit-title|--unit)
        unit=$2 && echo !! unit=${unit}
        shift
        ;;
        -x|--artefact_ext)
        artefact_ext=$2 && echo !! artefact_ext=${artefact_ext}
        shift
        ;;
        *)
        echo "Unknown option ${1}"
	echo "Usage: sh codedeploy_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket -u \${unit} -x \${artifact_ext}"
        exit 1
	;;
    esac
    shift
done

app="LI-Geodesy"

## Check variables
if [ -z ${app} ] ; then
    echo "Error: Application name has not been set"
	echo "Usage: sh codedeploy_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket -u \${unit} -x \${artifact_ext}"
fi

if [ -z ${env} ] ; then
    echo "Error: Environment has not been set"
	echo "Usage: sh codedeploy_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket -u \${unit} -x \${artifact_ext}"
fi

if [ -z ${deployment_config_name} ] ; then
    echo "Error: Deployment Config has not been set"
	echo "Usage: sh codedeploy_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket -u \${unit} -x \${artifact_ext}"
fi

if [ -z ${s3_bucket} ] ; then
    echo "Error: S3 bucket name has not been set"
	echo "Usage: sh codedeploy_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket -u \${unit} -x \${artifact_ext}"
fi

if [ -z ${unit} ] ; then
    echo "Error: Unit name type has not been set"
	echo "Usage: sh codedeploy_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket -u \${unit} -x \${artifact_ext}"
fi

if [ -z ${artefact_ext} ] ; then
    echo "Error: Artefact extension for key in deployment not found e.g. tar.gz"
	echo "Usage: sh codedeploy_deploy.sh -a \$app -e \$env -c \$deployment_config_name -b \$s3_bucket -u \${unit} -x \${artifact_ext}"
fi

## Declare variables
date=$(date +%d_%m_%y) && echo !! date=${date}
time=$(date +%H_%M_%S) && echo !! time=${time}
deployment_group_name=${env}-${app}-${unit}AsgCdg && echo -e !! "${Blue}" deployment_group_name=${deployment_group_name} "$Color_Off"
appenv=${env}-${app}-${unit}AsgCda && echo -e !! "${Yellow}" appenv=${appenv} "$Color_Off"
description_create_deployment="${deployment_group_name}-${date}-${time}" && echo !! description_create_deployment=${description_create_deployment}
key=${app}/${appenv}.${artefact_ext} && echo -e !! "${Red}" key=${key} "$Color_Off"
profile="geodesy"

## Code Deploy Push Directory
echo -e !! "${Blue}" Codedeploy push ${appenv} to s3://${s3_bucket}/${key} "$Color_Off"
aws deploy push --application-name ${appenv} --s3-location s3://${s3_bucket}/${key} --source codedeploy-${unit} --description ${description_create_deployment} --profile ${profile}


## Code Deploy Application from output
create_deployment="$(aws deploy create-deployment --application-name ${appenv} --deployment-config-name ${deployment_config_name} --deployment-group-name ${deployment_group_name} --description ${description_create_deployment} --s3-location bucket=${s3_bucket},bundleType=${artefact_ext},key=${key} --profile ${profile})"
echo -e !! "${Red}" create_deployment=${create_deployment} "$Color_Off"

# TEST
# This is a small script to check to see if a code deploy deployment has finished creating in AWS

## Variables
deployment_id="$(echo ${create_deployment} | python -c "
import sys
import json
id = json.loads(''.join(sys.stdin.readlines()))
print(id['deploymentId'])
"
)"

##Validate Stack is created
deploy_status="InProgress"

while [ 1 ]; do
    response=$(aws deploy get-deployment --deployment-id ${deployment_id} --profile ${profile} 2>&1)
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
