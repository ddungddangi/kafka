# 1단계: 빌드 스테이지
FROM gradle:8.5-jdk17-alpine AS builder

# 소스 코드 복사
WORKDIR /build
COPY . .

# 빌드 실행 (build/libs/*.jar 생성)
RUN gradle build -x test

# 2단계: 실제 런타임 이미지
FROM openjdk:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 결과물 복사
COPY --from=builder /build/build/libs/*.jar app.jar

# 포트 오픈
EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
