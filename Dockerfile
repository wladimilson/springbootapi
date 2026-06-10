# 1. Base stage: setup environment and source code
FROM eclipse-temurin:25-jdk-alpine AS base
WORKDIR /app
COPY . /app
RUN sed -i 's/\r$//' mvnw

# 2. Test stage: runs unit tests and generates JaCoCo coverage report
# Use `docker build --target test .` to run just the tests
FROM base AS test
RUN ./mvnw test jacoco:report

# 3. Builder stage: creates minimal JRE and packages the application
FROM base AS builder
# Added jdk.crypto.ec for JWT/RSA support
# Fixed --compress to use 'zip-6' (JDK 21+)
RUN jlink \
    --verbose \
    --add-modules java.base,java.compiler,java.instrument,java.management,java.naming,java.prefs,java.rmi,java.scripting,java.security.jgss,java.sql,jdk.unsupported,jdk.crypto.ec,jdk.management.agent,java.desktop \
    --compress zip-6 \
    --no-header-files \
    --no-man-pages \
    --strip-debug \
    --output /minimal-jre
RUN ./mvnw clean package -DskipTests

# 4. Runtime stage: final lightweight application image
FROM alpine:3.20 AS runtime

# Upgrade OS packages to apply security patches and install CA certificates
RUN apk update && \
    apk upgrade && \
    apk add --no-cache ca-certificates

# maintainer name
LABEL authors="Pedro Robson Leão <pedro.leao@gmail.com>"

# environment variable with appname
ENV APP=springbootapi-0.0.1-SNAPSHOT.jar
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Add app user.
ARG USER_NAME="appuser"
ARG USER_ID="1001"
ARG GROUP_NAME="apps"
ARG GROUP_ID="1001"
# Configure work directory.
ARG APP_DIR=/app
ARG SPRING_PROFILES_ACTIVE=local

RUN addgroup --gid ${GROUP_ID} ${GROUP_NAME} && \
    adduser --no-create-home --uid ${USER_ID} --ingroup ${GROUP_NAME} --disabled-password ${USER_NAME} && \
    mkdir ${APP_DIR} && \
    chown -R ${USER_NAME}:${GROUP_NAME} ${APP_DIR}

# Copy entrypoint script and make it executable
COPY --chown=${USER_NAME}:${GROUP_NAME} docker-entrypoint.sh ${APP_DIR}/entrypoint.sh
RUN chmod +x ${APP_DIR}/entrypoint.sh

# crate app workdir to runtime application
WORKDIR ${APP_DIR}

# Copy downsized JRE from builder image.
COPY --from=builder --chown=${USER_NAME}:${GROUP_NAME} /minimal-jre $JAVA_HOME

# copy build jar file from build image
COPY --from=builder --chown=${USER_NAME}:${GROUP_NAME} app/target/${APP} application.jar

# expose http access application port
EXPOSE 8080

# run application when run container
USER ${USER_NAME}:${GROUP_NAME}

ENV JSON_LOGS_ENABLED=true
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

ENTRYPOINT ["./entrypoint.sh"]
