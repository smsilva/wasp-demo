#!/bin/sh
echo "PWD....................: ${PWD}"
echo "JAVA_OPTS..............: ${JAVA_OPTS}"
echo "SPRING_PROFILES_ACTIVE.: ${SPRING_PROFILES_ACTIVE}"

java ${JAVA_OPTS} -jar entrypoint.jar
