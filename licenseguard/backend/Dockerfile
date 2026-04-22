FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy the cross-cutting library first
COPY cross-cutting ./cross-cutting

# Copy the backend code
COPY licenseguard/backend ./licenseguard/backend

# Build cross-cutting first
WORKDIR /app/cross-cutting
RUN ./mvnw clean install -DskipTests

# Build licenseguard
WORKDIR /app/licenseguard/backend
RUN ../../cross-cutting/mvnw clean package -DskipTests

# Final stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/licenseguard/backend/target/licenseguard-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8100
ENTRYPOINT ["java", "-jar", "app.jar"]
