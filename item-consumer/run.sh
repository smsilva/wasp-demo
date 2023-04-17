#!/bin/bash

docker run \
  --rm \
  -e SPRING_LOGGING_LEVEL_ROOT="${SPRING_LOGGING_LEVEL_ROOT-INFO}" \
  -e RABBITMQ_HOST="${RABBITMQ_HOST?}" \
  -e RABBITMQ_PORT="${RABBITMQ_PORT?}" \
  -e RABBITMQ_SSL_ENABLED="${RABBITMQ_SSL_ENABLED?}" \
  -e RABBITMQ_USERNAME="${RABBITMQ_USERNAME?}" \
  -e RABBITMQ_PASSWORD="${RABBITMQ_PASSWORD?}" \
  -e RABBITMQ_VIRTUAL_HOST="${RABBITMQ_VIRTUAL_HOST?}" \
  -e RABBITMQ_QUEUE_NAME_MAIN="${RABBITMQ_QUEUE_NAME_MAIN?}" \
  -e NEW_RELIC_ACCOUNT_ID="${NEW_RELIC_ACCOUNT_ID?}" \
  -e NEW_RELIC_LICENSE_KEY="${NEW_RELIC_LICENSE_KEY?}" \
  -e NEW_RELIC_API_KEY="${NEW_RELIC_API_KEY?}" \
  -e NEW_RELIC_LOG_LEVEL="${NEW_RELIC_LOG_LEVEL-info}" \
  -e NEW_RELIC_APPENDER_ENABLED="${NEW_RELIC_APPENDER_ENABLED-false}" \
  -e NEW_RELIC_ENVIRONMENT="${NEW_RELIC_ENVIRONMENT}" \
  wasp-item-consumer:latest
