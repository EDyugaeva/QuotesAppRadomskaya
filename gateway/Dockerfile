FROM openjdk:11.0.11-jre-slim
ARG JAR_FILE=target/gateway-0.0.1-SNAPSHOT.jar
EXPOSE 8090
COPY ${JAR_FILE} gateway.jar
ENTRYPOINT ["java", "-jar", "gateway.jar"]