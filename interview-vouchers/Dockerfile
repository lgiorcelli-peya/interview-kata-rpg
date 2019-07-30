FROM openjdk:11.0.3-jre

RUN groupadd --gid 1000 user   && useradd --uid 1000 --gid user --shell /bin/bash user

ENV NEW_RELIC_APP_NAME=""
ENV NEW_RELIC_LICENSE_KEY=""
ENV NEW_RELIC_DISTRIBUTED_TRACING=false

ENV SECURITY_OPTS="-Dnetworkaddress.cache.negative.ttl=0 -Dnetworkaddress.cache.ttl=0"
ENV SERVER_IP=""
ENV CLIENT_HEADERS=""
ENV ENVIRONMENT=""
ENV JAVA_TOOL_OPTIONS="-Dhttps.protocols=TLSv1.2"
ENV MAX_RAM_PERCENTAGE="-XX:MaxRAMPercentage=70"
ENV MIN_RAM_PERCENTAGE="-XX:MinRAMPercentage=70"
ENV NEW_RELIC_ARG="-javaagent:newrelic/newrelic.jar -Dnewrelic.config.distributed_tracing.enabled=$NEW_RELIC_DISTRIBUTED_TRACING"
ENV JAVA_OPTS="$JAVA_OPTS $NEW_RELIC_ARG $SECURITY_OPTS $MAX_RAM_PERCENTAGE $MIN_RAM_PERCENTAGE $JAVA_TOOL_OPTIONS"


EXPOSE $SERVER_PORT

COPY --chown=1000:1000 /voucher-api/src/main/resources/newrelic/newrelic.yml app/newrelic/
COPY --chown=1000:1000 /build/libs/newrelic.jar app/newrelic/newrelic.jar
COPY --chown=1000:1000 /voucher-api/build/libs/voucher-api-1.0-SNAPSHOT.jar app/api.jar

WORKDIR /app

USER 1000

ENTRYPOINT exec java $JAVA_OPTS -jar api.jar