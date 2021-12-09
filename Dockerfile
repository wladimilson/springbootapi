# origin image to build application
FROM maven:3-jdk-11 AS build

# maintainer name
LABEL maintainer = "Pedro Robson Leão <pedro.leao@gmail.com>"

# create app dir to build application
WORKDIR app

# copy source to docker build environment
COPY . .

# run build command
RUN mvn clean install -D maven.test.skip=true

# origin image to runtime application image
FROM openjdk:11-jre-slim AS runtime

# maintainer name
LABEL maintainer = "Pedro Robson Leão <pedro.leao@gmail.com>"

# environment variable with appname
ENV  APP=springbootapi-0.0.1-SNAPSHOT.jar

# crate app workdir to runtime application
WORKDIR app

#USER app

# copy build jar file from build image
COPY --from=build app/target/${APP} .

# expose http access application port
EXPOSE 8080

# run application when run container
ENTRYPOINT ["java","-jar", "springbootapi-0.0.1-SNAPSHOT.jar"]



