#!/bin/bash

set -euo pipefail

mvn package

docker build \
  --rm \
  --tag "wasp-item-relay:latest" .
