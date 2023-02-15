FROM openjdk:11.0.11-jre-slim
ARG JAR_FILE=target/quoteservice-0.0.1-SNAPSHOT.jar
EXPOSE 8082
COPY ${JAR_FILE} quoteservice.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "quoteservice.jar"]