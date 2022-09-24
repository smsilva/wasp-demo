FROM azul/zulu-openjdk-alpine:11-jre

ADD target/newrelic-agent.jar /opt/service/newrelic-agent.jar
ADD target/newrelic.yml       /opt/service/newrelic.yml
ADD entrypoint.sh             /opt/service/entrypoint.sh
ADD target/item-*.jar         /opt/service/entrypoint.jar

WORKDIR /opt/service

ENTRYPOINT ["/opt/service/entrypoint.sh"]
