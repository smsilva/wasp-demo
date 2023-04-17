#!/bin/sh
export JAVA_OPTS="${JAVA_OPTS} -javaagent:newrelic-agent.jar"
export JAVA_OPTS="${JAVA_OPTS} -Dnewrelic.config.file=newrelic.yml"

if [ -n "${NEW_RELIC_ENVIRONMENT}" ]; then
  export JAVA_OPTS="${JAVA_OPTS} -Dnewrelic.environment=${NEW_RELIC_ENVIRONMENT}"
fi

echo "PWD........................: ${PWD}"
echo "RABBITMQ_HOST..............: ${RABBITMQ_HOST}"
echo "RABBITMQ_PORT..............: ${RABBITMQ_PORT}"
echo "RABBITMQ_USERNAME..........: ${RABBITMQ_USERNAME}"
echo "RABBITMQ_PASSWORD..........: ${RABBITMQ_PASSWORD:0:5}"
echo "RABBITMQ_VIRTUAL_HOST......: ${RABBITMQ_VIRTUAL_HOST}"
echo "RABBITMQ_QUEUE_NAME_MAIN...: ${RABBITMQ_QUEUE_NAME_MAIN-teste}"
echo "RABBITMQ_SSL_ENABLED.......: ${RABBITMQ_SSL_ENABLED}"
echo "NEW_RELIC_ACCOUNT_ID.......: ${NEW_RELIC_ACCOUNT_ID}"
echo "NEW_RELIC_API_KEY..........: ${NEW_RELIC_API_KEY:0:10}"
echo "NEW_RELIC_LICENSE_KEY......: ${NEW_RELIC_LICENSE_KEY:0:5}"
echo "NEW_RELIC_LOG_LEVEL........: ${NEW_RELIC_LOG_LEVEL-fine}"
echo "NEW_RELIC_APPENDER_ENABLED.: ${NEW_RELIC_LOG_LEVEL-fine}"
echo "NEW_RELIC_ENVIRONMENT......: ${NEW_RELIC_ENVIRONMENT}"
echo "JAVA_OPTS..................: ${JAVA_OPTS}"
echo "SPRING_PROFILES_ACTIVE.....: ${SPRING_PROFILES_ACTIVE}"

java ${JAVA_OPTS} -jar entrypoint.jar
