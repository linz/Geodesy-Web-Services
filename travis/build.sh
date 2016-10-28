#!/usr/bin/env bash


nix-shell --command "mvn --settings ./travis/maven-settings.xml docker:build docker:run"

if [ ${TRAVIS_PULL_REQUEST} = 'false' ]; then
    nix-shell --command "mvn --settings ./travis/maven-settings.xml deploy"
else
    nix-shell --command "mvn --settings ./travis/maven-settings.xml test"
fi
