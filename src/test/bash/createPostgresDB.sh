#!/usr/bin/env bash

# Quick and Dirty script to set up postgres DB and Schema for geodesy-domain-model
# TODO: consider security and passwords.

#PREREQS
# Install Postgres  [ $ sudo pacman -S postgresql ]
# Install the PostGIS Libraries [ $ sudo pacman -S postgis ]

# run this script as the postgres installation user ( usually postgres )

# dbname = geodesydb
# username = geodesy
# password = geodesypw

# A quick note on roles, schemas and users
# a user is a role with a password
# a schema is a namespace within a database

# A quick note on POSTGIS
# install the POSTGIS libraries, as per your OS
# If you create the POSTGIS extension in the public schema, all databases you create will inherit it
# and the CREATE EXTENSION below will error. The error can be ignored.




psql <<EOF
drop schema if exists geodesy;
drop database if exists geodesydb;
drop role if exists geodesy;

create user geodesy with password 'geodesypw';

create database geodesydb owner geodesy;

EOF

# enable the POSTGIS Extension in the new database
psql -d geodesydb -c "CREATE EXTENSION postgis;"


# create schema and test
psql -U geodesy  geodesydb   <<EOF

create schema geodesy authorization geodesy;

-- test that it can create a table

create table geodesy.x (
    c1        char(5) constraint firstkey primary key,
    c2       varchar(40) not null,
    c3         integer not null,
    c4   date,
    c5        varchar(10),
    c6         interval hour to minute,
	geom GEOMETRY(Point, 26910)
);

insert into x values ('abcde','sometext',123,current_date,'abc', 	interval '1 day', ST_GeomFromText('POINT(0 0)', 26910) );


select * from geodesy.x;
EOF


#list out what we've created
psql -U geodesy  geodesydb   <<EOF
\c
\d
EOF

#clean up
psql -U geodesy  geodesydb   <<EOF
drop table x;
EOF

