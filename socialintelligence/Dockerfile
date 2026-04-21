FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY cross-cutting/pom.xml cross-cutting/pom.xml
COPY cross-cutting/cc-starter cross-cutting/cc-starter
COPY socialintelligence/backend socialintelligence/backend
RUN cd cross-cutting && ./mvnw install -DskipTests
RUN cd socialintelligence/backend && ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/socialintelligence/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
