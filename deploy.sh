#!/usr/bin/env bash

COMMAND=$1
ENV=$2

AWS_PROFILE=geodesy
ENV_FILE=infra-${ENV}.yaml
STACK_NAME="${ENV^}"Geodesy

RDS_MASTER_USERNAME_KEY=${STACK_NAME}GeodesyRdsUsername
RDS_MASTER_PASSWORD_KEY=${STACK_NAME}GeodesyRdsPassword

case ${COMMAND} in
"create")
    RDS_MASTER_USERNAME=postgres
    RDS_MASTER_PASSWORD=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 13)
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

python3 generate_amz_yaml.py -b infra-base.yaml -v ${ENV_FILE} -o infra.yaml
python3 amazonia/amazonia/amz.py -y infra.yaml -d geodesy_ga_defaults.yaml -t infra.json

aws --profile ${AWS_PROFILE} cloudformation ${COMMAND}-stack --stack-name ${STACK_NAME} --template-body file://infra.json --parameters \
    ParameterKey=${RDS_MASTER_USERNAME_KEY},ParameterValue=${RDS_MASTER_USERNAME} \
    ParameterKey=${RDS_MASTER_USERNAME_KEY},ParameterValue=${RDS_MASTER_PASSWORD}

if (( $? == 0 && ${COMMAND} == "create" )); then
    credstash -p ${AWS_PROFILE} delete ${RDS_MASTER_USERNAME_KEY}
    credstash -p ${AWS_PROFILE} delete ${RDS_MASTER_PASSWORD_KEY}
    credstash -p ${AWS_PROFILE} put ${RDS_MASTER_USERNAME_KEY} ${RDS_MASTER_USERNAME}
    credstash -p ${AWS_PROFILE} put ${RDS_MASTER_PASSWORD_KEY} ${RDS_MASTER_PASSWORD}
fi

