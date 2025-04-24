# Dockerfile (openjdk 단일 버전)
FROM openjdk:17-alpine
WORKDIR /app
COPY demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

