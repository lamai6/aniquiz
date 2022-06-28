FROM openjdk:17-jdk-alpine
RUN apk update && apk upgrade
RUN apk add inotify-tools dos2unix
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME