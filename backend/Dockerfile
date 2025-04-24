# 1단계: 빌드용 이미지 (Gradle 실행 환경)
FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /app

# Gradle 캐시 최적화를 위해 먼저 의존성만 복사해서 설치
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build -x test --no-daemon

# 2단계: 실행용 이미지 (경량 JDK)
FROM openjdk:17-alpine
WORKDIR /app

# 빌드 결과 jar 복사 (위 단계에서 생성된 jar 파일)
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 오픈 (Spring Boot 기본 포트)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
