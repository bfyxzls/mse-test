# 第一阶段：构建应用
FROM maven:3.6.3-jdk-11 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline # Maven在离线模式下下载项目的所有依赖

COPY src ./src
RUN mvn package

# 第二阶段：运行应用
FROM openjdk:8u322-jre-slim-buster
WORKDIR /app
COPY --from=builder /app/target/test-app-1.0.0-SNAPSHOT.jar ./app.jar
CMD ["java", "-jar", "app.jar"]
