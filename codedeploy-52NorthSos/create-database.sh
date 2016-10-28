#!/usr/bin/env bash

# Prerequesites
# RDS, PostgreSQL with PostGIS libraries
# Credstash keys ${ENV}GeodesyDbRdsUsername, ${ENV}GeodesyDbRdsPassword, ${ENV}GeodesyDbUsername, ${ENV}GeodesyDbPassword

. ${BASH_SOURCE%/*}/env.sh

DB_SCHEMA="sos"

PSQL_AS_MASTER_USER="PGPASSWORD=${RDS_MASTER_PASSWORD} psql --host=${RDS_ENDPOINT} --port=5432 --username ${RDS_MASTER_USERNAME}  --dbname=${DB_NAME}"
PSQL_AS_DB_USER="PGPASSWORD=${DB_PASSWORD} psql --host=${RDS_ENDPOINT} --port=5432 --username ${DB_USERNAME}  --dbname=${DB_NAME}"

# Only proceed if the sos schema containing tables does not already exist
EXISTING_SCHEMA=$(eval "${PSQL_AS_MASTER_USER} -qtA -c \"SELECT schema_name FROM information_schema.schemata WHERE schema_name = '${DB_SCHEMA}';\"")

if [[ ${EXISTING_SCHEMA} == ${DB_SCHEMA} ]]; then

  EXISTING_TABLES=$(eval "${PSQL_AS_MASTER_USER} -qtA -c \"SELECT count(*) from information_schema.tables where table_schema = '${DB_SCHEMA}';\"")

  if [[ ${EXISTING_TABLES} -gt 0 ]]; then
    echo "Exiting, schema ${DB_SCHEMA} with tables already exists."
    exit 0
  fi

fi

# Ensure db user exists
EXISTING_USER=$(eval "${PSQL_AS_MASTER_USER} -qtA -c \"SELECT count(*) FROM pg_catalog.pg_user WHERE usename = '${DB_USERNAME}';\"")

if [ $EXISTING_USER -eq 0 ]; then
echo "Creating database user"
PGPASSWORD=${RDS_MASTER_PASSWORD} psql --host=${RDS_ENDPOINT} --port=5432 --username ${RDS_MASTER_USERNAME}  --dbname=${DB_NAME} << EOF
create user ${DB_USERNAME} with password '${DB_PASSWORD}';
alter database "${DB_NAME}" owner to ${DB_USERNAME};
EOF
fi

# Ensure the schema exists
PGPASSWORD=${RDS_MASTER_PASSWORD} psql --host=${RDS_ENDPOINT} --port=5432 --username ${RDS_MASTER_USERNAME}  --dbname=${DB_NAME} << EOF
drop schema if exists ${DB_SCHEMA} CASCADE;
create schema ${DB_SCHEMA} authorization ${DB_USERNAME};
EOF

# Populate the schema with 52 North SOS database objects
PGPASSWORD=${DB_PASSWORD} psql --host=${RDS_ENDPOINT} --port=5432 --username ${DB_USERNAME}  --dbname=${DB_NAME} << EOF
\set schema '${DB_SCHEMA}'
\i ${BASH_SOURCE%/*}/PG_series_script_create.sql
EOF
