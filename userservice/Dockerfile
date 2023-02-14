FROM openjdk:11.0.11-jre-slim
ARG JAR_FILE=target/userService-0.0.1-SNAPSHOT.jar
EXPOSE 8081
COPY ${JAR_FILE} userservice.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "userservice.jar"]