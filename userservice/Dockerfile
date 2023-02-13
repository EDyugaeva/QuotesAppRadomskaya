FROM openjdk:11.0.11-jre-slim
ARG JAR_FILE=target/demo_service_1-0.0.1-SNAPSHOT.jar
EXPOSE 8081
COPY ${JAR_FILE} service1.jar
ENTRYPOINT ["java", "-jar", "service1.jar"]