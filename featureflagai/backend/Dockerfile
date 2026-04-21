FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY cross-cutting /app/cross-cutting
COPY featureflagai/backend /app/featureflagai/backend
WORKDIR /app/featureflagai/backend
RUN ../../cross-cutting/mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/featureflagai/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]