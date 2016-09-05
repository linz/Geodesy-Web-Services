#!/usr/bin/env bash

# Prerequesites
# RDS, PostgreSQL with PostGIS libraries
# Credstash keys ${ENV}GeodesyDbRdsUsername, ${ENV}GeodesyDbRdsPassword, ${ENV}GeodesyDbUsername, ${ENV}GeodesyDbPassword

. ${BASH_SOURCE%/*}/env.sh

DB_SCHEMA=${DB_USERNAME}

PSQL_AS_MASTER_USER="PGPASSWORD=${RDS_MASTER_PASSWORD} psql --host=${RDS_ENDPOINT} --port=5432 --username ${RDS_MASTER_USERNAME}  --dbname=${DB_NAME}"
PSQL_AS_DB_USER="PGPASSWORD=${DB_PASSWORD} psql --host=${RDS_ENDPOINT} --port=5432 --username ${DB_USERNAME}  --dbname=${DB_NAME}"

EXISTING_SCHEMA=$(eval "${PSQL_AS_MASTER_USER} -qtA -c \"SELECT schema_name FROM information_schema.schemata WHERE schema_name = '${DB_SCHEMA}';\"")

if [[ ${EXISTING_SCHEMA} == ${DB_SCHEMA} ]]; then
  echo "Exiting, schema ${DB_SCHEMA} already exists."
  exit 0
fi

PGPASSWORD=${RDS_MASTER_PASSWORD} psql --host=${RDS_ENDPOINT} --port=5432 --username ${RDS_MASTER_USERNAME}  --dbname=${DB_NAME} << EOF
drop schema if exists ${DB_SCHEMA};
drop role if exists ${DB_USERNAME};
create user ${DB_USERNAME} with password '${DB_PASSWORD}';
grant ${DB_SCHEMA} to postgres;
alter database "${DB_NAME}" owner to ${DB_USERNAME};
create schema ${DB_SCHEMA} authorization ${DB_USERNAME};
create extension postgis;
EOF
