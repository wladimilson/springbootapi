#!/bin/sh
echo "Java Opts=${JAVA_OPTS}"
exec java ${JAVA_OPTS} \
  --add-opens java.base/java.time=ALL-UNNAMED \
  -Duser.timezone=UTC \
  -DjsonLogsEnabled=true \
  -Djava.security.egd=file:/dev/./urandom \
  -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
  -XshowSettings:vm \
  -XX:MaxRAMPercentage=90 \
  -jar application.jar
