FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY ./build/libs/Internet-benchmark-backend-0.0.1-SNAPSHOT.jar /app/benchmark.jar

ENTRYPOINT ["java","-jar","benchmark.jar"]
