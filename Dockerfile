#단계 1
# Java 21 JDK 이미지를 빌드 단계의 베이스 이미지로 사용
# -> Spring Boot 프로젝트를 jar 파일로 빌드하려면 Java 컴파일 도구가 포함된 JDK가 필요
FROM eclipse-temurin:21-jdk AS builder

# 컨테이너 내부의 작업 디렉토리를 /app으로 설정
WORKDIR /app

#Gradle 관련 파일들 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .

# 실제 애플리케이션 소스코드를 컨테이너의 /app/src 디렉토리로 복사
# 위에서 말한 자주 변경되는 영역이므로 Gradle 설정 파일보다 나중에 복사
COPY src src

# Spring Boot 실행 가능한 jar 파일을 생성(아래에서 쓰는 것)
# clean은 기존 빌드 결과를 제거하고, bootJar는 실행 가능한 Spring Boot jar를 생성.
RUN ./gradlew clean bootJar


# 단계2
# Java 21 JRE가 포함된 이미지를 실행 단계의 베이스 이미지로 사용
# 실행 단계에서는 이미 만들어진 jar 파일만 실행하면 되므로 컴파일 도구가 포함된 JDK가 필요X
FROM eclipse-temurin:21-jre

# 최종 실행 이미지에서도 작업 디렉토리를 /app으로 설정
WORKDIR /app

# builder 단계에서 생성된 jar 파일을 현재 실행 이미지의 /app/app.jar 이름으로 복사
# --from=builder : 위에서 AS builder로 이름 붙인 빌드 단계에서 파일을 가져온다는 뜻
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# 컨테이너가 시작될 때 Spring Boot jar 파일을 실행
# 즉 컨테이너 실행 시 java -jar app.jar 명령이 실행
ENTRYPOINT ["java", "-jar", "app.jar"]