FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
COPY src ./src
# Install cc-starter first in real scenario, here we just package
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8101
ENTRYPOINT ["java", "-jar", "app.jar"]
