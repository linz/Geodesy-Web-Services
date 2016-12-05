#!/usr/bin/env bash

PGPASSWORD=postgres psql -U postgres <<EOF
drop database if exists geodesydb;
drop database if exists geodesy_baseline_db;
drop role if exists geodesy;
create user geodesy with password 'geodesypw';
create database geodesydb owner geodesy;
create database geodesy_baseline_db owner geodesy;
EOF

PGPASSWORD=postgres psql -U postgres geodesydb <<EOF
create extension postgis;
create schema geodesy authorization geodesy;
EOF

PGPASSWORD=postgres psql -U postgres geodesy_baseline_db <<EOF
create extension postgis;
create schema geodesy authorization geodesy;
EOF
