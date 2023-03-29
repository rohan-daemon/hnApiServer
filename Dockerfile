#docker file
FROM maven:3.6.3-jdk-13 AS maven-service
RUN mkdir -p /app/code
COPY src /app/code/src
ADD pom.xml /app/code/
WORKDIR /app/code
RUN mvn clean install -Dmaven.test.skip=true

#multi layer
FROM openjdk:13-jre AS jre-service
RUN mkdir -p /app/code
COPY --from=maven-service /app/code/target/HnApiDemo-0.0.1-SNAPSHOT.jar /app/code
WORKDIR /app/code
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=default","-jar","HnApiDemo-0.0.1-SNAPSHOT.jar"]