FROM openjdk:11.0.11-jre-slim
ARG JAR_FILE=target/voteservice-0.0.1-SNAPSHOT.jar
EXPOSE 8083
COPY ${JAR_FILE} voteservice.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "voteservice.jar"]