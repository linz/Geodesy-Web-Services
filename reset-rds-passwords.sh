#!/usr/bin/env bash

# Generate new passwords for postgres master user and geodesy user
# Use aws cli to connect and update master user in rds ( depends on AWS credentials being set)
# Connect as master and update geodesy pw 
# Save geodesy password to make it available to tomcat

#DEPENDENCIES
#postgres psql client must be available
# aws cli must be configured with the geodesy profile

NEW_RDS_MASTER_PASSWORD=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | head -c 32) 
NEW_GEODESY_PASSWORD=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | head -c 32)

DB_ID=$1

AWS_CMD="aws --region ap-southeast-2"

${AWS_CMD} rds modify-db-instance --db-instance-identifier ${DB_ID}  --master-user-password ${NEW_RDS_MASTER_PASSWORD}

#echo $( aws --region ap-southeast-2 rds describe-db-instances --db-instance-identifier ${DB_ID} | grep "resetting-master-credentials" | wc -l)

# it takes ages for the instance status to go to "DBInstanceStatus": "resetting-master-credentials"
#, and we need to be sure that it happens so we don't jump in to try to change the password to soon,
#, so wait till it start resetting the password
echo "waiting for reset to start"
while [ "$(${AWS_CMD} rds describe-db-instances --db-instance-identifier ${DB_ID} | grep resetting-master-credentials | wc -l)" -eq 0 ]
do
  printf "."
  sleep 2
done

# and when it's started resetting it,  wait till it completed....

echo "waiting for reset to complete"
while [ "$(${AWS_CMD} rds describe-db-instances --db-instance-identifier ${DB_ID} | grep resetting-master-credentials | wc -l)" -ne 0 ]
do
  printf "."
  sleep 2
done

#build new pgpass file
RDS_ENDPOINT=$(${AWS_CMD} rds describe-db-instances --db-instance-identifier ${DB_ID} | grep Address | awk -F'"' {'print $4'})
echo ${RDS_ENDPOINT}:5432:geodesy:postgres:${NEW_RDS_MASTER_PASSWORD} >~/.pgpass
echo ${RDS_ENDPOINT}:5432:geodesy:geodesy:${NEW_GEODESY_PASSWORD} >>~/.pgpass
chmod 600 ~/.pgpass


#now we can connect with the new master password and set the geodesy password to it's new value

psql --host=${RDS_ENDPOINT} --port=5432 --username postgres   --dbname=geodesy <<EOF
alter user geodesy with password '${NEW_GEODESY_PASSWORD}';
EOF

sed -i 's/${geodesy-db-url}/jdbc:postgresql:\/\/'"${RDS_ENDPOINT}/" /usr/share/tomcat8/webapps/ROOT/META-INF/context.xml
sed -i 's/${geodesy-db-password}/'"${NEW_GEODESY_PASSWORD}/" /usr/share/tomcat8/webapps/ROOT/META-INF/context.xml
