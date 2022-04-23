#!/bin/bash
helm repo add bitnami https://charts.bitnami.com/bitnami

helm upgrade rabbitmq bitnami/rabbitmq \
  --install \
  --namespace rabbitmq \
  --create-namespace \
  --wait

export RABBITMQ_USER_NAME="user"
export RABBITMQ_PASSWORD="$(kubectl get secret --namespace rabbitmq rabbitmq -o jsonpath="{.data.rabbitmq-password}" | base64 --decode)"
export RABBITMQ_ERLANG_COOKIE="$(kubectl get secret --namespace rabbitmq rabbitmq -o jsonpath="{.data.rabbitmq-erlang-cookie}" | base64 --decode)"

echo "RABBITMQ_USER_NAME.....: ${RABBITMQ_USER_NAME}"
echo "RABBITMQ_PASSWORD......: ${RABBITMQ_PASSWORD:0:3}"
echo "RABBITMQ_ERLANG_COOKIE.: ${RABBITMQ_ERLANG_COOKIE:0:5}"
