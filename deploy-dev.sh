#!/usr/bin/env bash
export AWS_PROFILE=geodesy
PATH=~/.local/bin:$PATH
ENV=Dev make clean stack
