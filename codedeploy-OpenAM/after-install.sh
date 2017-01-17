#!/usr/bin/env bash

. ${BASH_SOURCE%/*}/env.sh

# Apply subsititions to config file
OPENAM_CONFIG_FILE=/opt/openam/tools/config/config.file

sed -i 's/${OPENAM_SERVER_URL}/'"${OPENAM_SERVER_FQDN}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_ADMIN_PWD}/'"${OPENAM_ADMIN_PWD}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_AMLDAPUSERPASSWD}/'"${OPENAM_AMLDAPUSERPASSWD}/" $OPENAM_CONFIG_FILE
sed -i 's/${OPENAM_DS_DIRMGRPASSWD}/'"${OPENAM_DS_DIRMGRPASSWD}/" $OPENAM_CONFIG_FILE

# Wait if load balancer is failing health check (Configuration process requires http access to the
# OpenAM application via the load balancer)
until curl -s --head --connect-timeout 2 https://${OPENAM_SERVER_FQDN}/openam/images/gradlogsides.jpg | head -n 1 | grep 200 > /dev/null
do
  echo "waiting 10 seconds for load balancer to resume passing requests to openam server"
  sleep 10
done

# Apply the configuration
java -jar /opt/openam/tools/config/openam-configurator-tool-13.5.0.jar -f /opt/openam/tools/config/config.file
