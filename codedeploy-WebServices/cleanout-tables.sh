#!/usr/bin/env bash

DB_ID=$1

if [ "${DB_ID}" == "geodesy-dev" ]; then
	AWS_CMD="aws --region ap-southeast-2"
	RDS_ENDPOINT=$(${AWS_CMD} rds describe-db-instances --db-instance-identifier ${DB_ID} | grep Address | awk -F'"' {'print $4'})

	psql --host=${RDS_ENDPOINT} --port=5432 --username geodesy   --dbname=geodesy << 'EOF'
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

