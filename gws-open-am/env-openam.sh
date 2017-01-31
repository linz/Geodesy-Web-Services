#!/usr/bin/env bash

set -e

# Base directories
export OPENAM_BASE_DIR=/opt/openam
export TMP_DIR=/tmp/openam

# Tools
export ADMIN_TOOLS_DIR=$OPENAM_BASE_DIR/tools
export SSOADM=$ADMIN_TOOLS_DIR/admin/openam/bin/ssoadm
export EXPORT_LDIF=$OPENAM_BASE_DIR/opends/bin/export-ldif
export IMPORT_LDIF=$OPENAM_BASE_DIR/opends/bin/import-ldif

# Configuration files
export CONFIG_DIR=$TMP_DIR/config
export OPENAM_CFG_FILE=$CONFIG_DIR/openam-config
export SERVICE_CFG_FILE=$CONFIG_DIR/service-cfg.xml
export DIRECTORY_DATA_FILE=$CONFIG_DIR/embedded-directory-data.ldif
export KEYS_DIR=$CONFIG_DIR/keys

# $ADMIN_TOOLS_DIR/admin/setup relies on a hard-coded path to chmod
ln -sf "$(which chmod)" /bin/chmod

# Passwords
ADMIN_PWD=G2dioga1
DS_DIRMGRPASSWD=G2dioga123
