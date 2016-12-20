#!/usr/bin/env nix-shell
#!nix-shell ../shell.nix -i bash

set -e

# A local installation of maven prefers to run the global installation, if available.
sudo rm /etc/mavenrc

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
    mvn --settings ./travis/maven-settings.xml deploy -pl '!gws-system-test'
    mvn --settings ./travis/maven-settings.xml site-deploy -DskipUnitTests -pl gws-core
else
    mvn --settings ./travis/maven-settings.xml verify -pl '!gws-system-test'
fi
