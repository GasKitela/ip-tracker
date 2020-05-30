FROM openjdk:8u201-jre-alpine3.9
RUN mkdir /app
COPY ./target/scala-2.12/ml-ip-tracker-assembly-*.jar /app/
WORKDIR /app
ENTRYPOINT java -jar ml-ip-tracker-assembly-*.jar