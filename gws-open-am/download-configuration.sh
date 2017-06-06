#!/usr/bin/env bash

docker-compose exec open-am /tmp/openam/export-configuration.sh
docker cp "$(docker-compose ps -q open-am):/tmp/openam/configuration" .
