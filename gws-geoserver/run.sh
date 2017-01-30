#!/usr/bin/env bash

until [ "`curl http://web:8080/ | grep 'gnssAntenna'`" != "" ];
do
  echo --- sleeping for 5 seconds
  sleep 5
done

echo gws-web is ready!

catalina.sh run
