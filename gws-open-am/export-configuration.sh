#!/usr/bin/env bash

. /env.sh
. /env-openam.sh

# Prepare local filesystem for export files
rm -rf "$CONFIG_DIR"
mkdir "$CONFIG_DIR"
mkdir "$KEYS_DIR"

# Export OpenAM Service Configuration
$SSOADM export-svc-cfg -e geodenc -u amadmin -f "$ADMIN_TOOLS_DIR/admin/passwdfile" -o "$SERVICE_CFG_FILE"

# Export embedded LDAP data directory to LDIF file
"$EXPORT_LDIF"\
 --ldifFile "$DIRECTORY_DATA_FILE"\
 --backendID userRoot\
 --appendToLDIF\
 --hostName localhost\
 --port 4444\
 --bindDN cn=Directory\ Manager\
 --bindPassword G2dioga123\
 --trustAll\
 --noPropertiesFile

cp "$OPENAM_BASE_DIR/openam/.keypass" "$KEYS_DIR/."
cp "$OPENAM_BASE_DIR/openam/keystore.jceks" "$KEYS_DIR/."
cp "$OPENAM_BASE_DIR/openam/keystore.jks" "$KEYS_DIR/."
cp "$OPENAM_BASE_DIR/openam/.storepass" "$KEYS_DIR/."
