#!/usr/bin/env bash

COMMAND=$1
ENV=$2

AWS_PROFILE=geodesy
ENV_FILE=infra-${ENV}.yaml
STACK_NAME="${ENV^}"OpenAM

# RDS Database credentials
RDS_MASTER_USERNAME_PARAM_NAME=DbMasterUsername
RDS_MASTER_PASSWORD_PARAM_NAME=DbMasterPassword

RDS_MASTER_USERNAME_KEY=${STACK_NAME}${RDS_MASTER_USERNAME_PARAM_NAME}
RDS_MASTER_PASSWORD_KEY=${STACK_NAME}${RDS_MASTER_PASSWORD_PARAM_NAME}

DB_USERNAME_KEY=${STACK_NAME}DbUsername
DB_PASSWORD_KEY=${STACK_NAME}DbPassword

# OpenAM Credentials
OPENAM_ADMIN_PWD_KEY=${STACK_NAME}OpenAmAdminPassword
OPENAM_AMLDAPUSERPASSWD_KEY=${STACK_NAME}OpenAmLdapUserPassword
OPENAM_DS_DIRMGRPASSWD_KEY=${STACK_NAME}OpenAmDsDirMgrPassword

case ${COMMAND} in
"create")
    RDS_MASTER_USERNAME=postgres
    RDS_MASTER_PASSWORD=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 13)

    DB_USERNAME=geodesy
    DB_PASSWORD=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 13)

    OPENAM_ADMIN_PWD=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 13)
    OPENAM_AMLDAPUSERPASSWD=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 13)
    OPENAM_DS_DIRMGRPASSWD=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 13)

    ;;
"update")
    RDS_MASTER_USERNAME=$(credstash -p ${AWS_PROFILE} get ${RDS_MASTER_USERNAME_KEY})
    RDS_MASTER_PASSWORD=$(credstash -p ${AWS_PROFILE} get ${RDS_MASTER_PASSWORD_KEY})

    ;;
*)
    echo "Usage: deploy-dev.sh (create|update) (dev|test|prod)"
    exit 1
    ;;
esac

python3 generate-infra-yaml.py -b infra-base_openam.yaml -v ${ENV_FILE} -o infra.yaml
python3 amazonia/amazonia/amz.py -y infra.yaml -d infra-defaults.yaml -t infra.json

aws --profile ${AWS_PROFILE} s3 cp infra.json s3://geodesy-stack-template/infra-${ENV}.json

aws --profile ${AWS_PROFILE} cloudformation ${COMMAND}-stack --stack-name ${STACK_NAME} \
    --template-url https://s3-ap-southeast-2.amazonaws.com/geodesy-stack-template/infra-${ENV}.json --parameters \
#    ParameterKey=environment,ParameterValue=${ENV^}
#    ParameterKey=${RDS_MASTER_USERNAME_PARAM_NAME},ParameterValue=${RDS_MASTER_USERNAME} \
#    ParameterKey=${RDS_MASTER_PASSWORD_PARAM_NAME},ParameterValue=${RDS_MASTER_PASSWORD}

if [[ $? == 0 && ${COMMAND} == "create" ]]; then
#    credstash -p ${AWS_PROFILE} put -a ${RDS_MASTER_USERNAME_KEY} ${RDS_MASTER_USERNAME}
#    credstash -p ${AWS_PROFILE} put -a ${RDS_MASTER_PASSWORD_KEY} ${RDS_MASTER_PASSWORD}
#    credstash -p ${AWS_PROFILE} put -a ${DB_USERNAME_KEY} ${DB_USERNAME}
#    credstash -p ${AWS_PROFILE} put -a ${DB_PASSWORD_KEY} ${DB_PASSWORD}

    # Only store OpenAm passwords if not already in credstash (recreating the passwords will
    # break an OpenAM installation when built using a previously exported configuration).
    credstash -p ${AWS_PROFILE} get ${OPENAM_ADMIN_PWD_KEY} &>/dev/null
    if [ ! $? -eq 0 ]; then
         credstash -p ${AWS_PROFILE} put -a ${OPENAM_ADMIN_PWD_KEY} ${OPENAM_ADMIN_PWD}
    fi
    credstash -p ${AWS_PROFILE} get ${OPENAM_AMLDAPUSERPASSWD_KEY} &>/dev/null
    if [ ! $? -eq 0 ]; then
         credstash -p ${AWS_PROFILE} put -a ${OPENAM_AMLDAPUSERPASSWD_KEY} ${OPENAM_AMLDAPUSERPASSWD}
    fi
    credstash -p ${AWS_PROFILE} get ${OPENAM_DS_DIRMGRPASSWD_KEY} &>/dev/null
    if [ ! $? -eq 0 ]; then
         credstash -p ${AWS_PROFILE} put -a ${OPENAM_DS_DIRMGRPASSWD_KEY} ${OPENAM_DS_DIRMGRPASSWD}
    fi

fi
