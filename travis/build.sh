#!/usr/bin/env nix-shell
#!nix-shell ../shell.nix -i bash

# A local installation of maven prefers to run the global installation, if available.
sudo rm /etc/mavenrc

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
    mvn --settings ./travis/maven-settings.xml deploy
    mvn --settings ./travis/maven-settings.xml -Psite site-deploy
else
    mvn --settings ./travis/maven-settings.xml test
fi
