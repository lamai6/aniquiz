#!/bin/bash
chmod +x mvnw
dos2unix mvnw

./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" &
while inotifywait -r -e CLOSE_WRITE,MODIFY --format '%:e %f' "src/main/"
do
  ./mvnw compile
done
