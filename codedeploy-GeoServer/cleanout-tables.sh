#!/usr/bin/env bash

. ${BASH_SOURCE%/*}/env.sh

if [[ ${ENV,,} == "dev" ]]; then
    PGPASSWORD=${DB_PASSWORD} psql -qtA --host=${RDS_ENDPOINT} --port=5432 --username ${DB_USERNAME} --dbname=${DB_NAME} << 'EOF'
DO
$func$
BEGIN
    EXECUTE
    (SELECT 'TRUNCATE TABLE '
             || string_agg( 'geodesy.' || quote_ident(tablename), ', ')
             || ' CASCADE'
     FROM   pg_tables
     WHERE  schemaname = 'geodesy'
        AND    tablename not in ('databasechangelog', 'databasechangeloglock')
     );
END
$func$;
EOF
    echo "Cleaned out tables in dev database"
else
    echo "Not dev database, not cleaning tables"
fi

