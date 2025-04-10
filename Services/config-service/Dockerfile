FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/config-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8888