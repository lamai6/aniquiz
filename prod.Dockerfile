FROM maven:3.8.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build
COPY pom.xml /build/pom.xml
RUN mvn dependency:go-offline

ARG SPRING_DATASOURCE_URL
COPY src /build/src
RUN mvn install
RUN mkdir -p target/dependency
WORKDIR /build/target/dependency
RUN jar -xf ../*.jar

FROM openjdk:17-jdk-alpine

RUN addgroup -S spring && adduser -S springuser -G spring -H
USER springuser
EXPOSE 8080
ARG DEPENDENCY=/build/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
ENTRYPOINT java -cp app:app/lib/* $STARTUP_CLASS