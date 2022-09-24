#!/bin/bash

set -euo pipefail

mvn package

docker build \
  --tag "wasp-item-consumer:latest" .
