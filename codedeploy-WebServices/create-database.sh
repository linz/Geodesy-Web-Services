#!/usr/bin/env bash

# Prerequesites
# RDS, PostgreSQL with PostGIS libraries
# Credstash keys ${ENV}GeodesyDbRdsUsername, ${ENV}GeodesyDbRdsPassword, ${ENV}GeodesyDbUsername, ${ENV}GeodesyDbPassword

# TODO
# a .pgpass file is create and updated with the current passwords
# and it will look something like this
#geodesy-dev.cxm7lrsl3bbf.ap-southeast-2.rds.amazonaws.com:5432:geodesy:postgres:postgresmasterpassword
#geodesy-dev.cxm7lrsl3bbf.ap-southeast-2.rds.amazonaws.com:5432:geodesydb:geodesy:geodesydbuserpassword

CREDSTASH="/usr/local/bin/credstash -r ap-southeast-2"

# temporarily hard-coded
ENV=Dev

# temporarily hard-coded to the dev database endpoint
DB_ENDPOINT=dd1iyix40zjic7t.cxm7lrsl3bbf.ap-southeast-2.rds.amazonaws.com

DB_NAME=GeodesyDb

RDS_MASTER_USERNAME=$(${CREDSTASH} get ${ENV}GeodesyDbRdsUsername)
RDS_MASTER_PASSWORD=$(${CREDSTASH} get ${ENV}GeodesyDbRdsPassword)
DB_USERNAME=$(${CREDSTASH} get ${ENV}GeodesyDbUsername)
DB_PASSWORD=$(${CREDSTASH} get ${ENV}GeodesyDbPassword)
DB_SCHEMA=${DB_USERNAME}

PGPASSWORD=${RDS_MASTER_PASSWORD} psql --host=${DB_ENDPOINT} --port=5432 --username ${RDS_MASTER_USERNAME}  --dbname=${DB_NAME}  << EOF
drop schema if exists ${DB_SCHEMA};
drop role if exists ${DB_USERNAME};
create user ${DB_USERNAME} with password '${DB_PASSWORD}';
grant ${DB_SCHEMA} to postgres;
alter database "${DB_NAME}" owner to ${DB_USERNAME};
EOF

# enable the POSTGIS Extension in the new database
PGPASSWORD=${RDS_MASTER_PASSWORD} psql --host=${DB_ENDPOINT} --port=5432 --username ${RDS_MASTER_USERNAME} --dbname=${DB_NAME}  -c "CREATE EXTENSION postgis;"

# create schema and test
PGPASSWORD=${DB_PASSWORD} psql --host=${DB_ENDPOINT} --port=5432 --username ${DB_USERNAME} --dbname=${DB_NAME}  << EOF
create schema ${DB_SCHEMA} authorization ${DB_USERNAME};
EOF

