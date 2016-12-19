#!/usr/bin/env bash

pg_dumpall -U postgres -h localhost -p 5434 --globals-only --no-tablespaces > target/site/create-db.sql
pg_dump -U geodesy -h localhost -p 5434 --clean --create geodesydb >> target/site/create-db.sql
