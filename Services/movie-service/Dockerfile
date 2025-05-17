FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/* app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8081