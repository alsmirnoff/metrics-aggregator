# сборка
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Финальная стадия
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/metrics-aggregator-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]