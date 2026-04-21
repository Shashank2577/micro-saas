FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace

# Copy cross-cutting which is needed for dependencies
COPY cross-cutting /workspace/cross-cutting

# Copy the app
COPY marketplacehub /workspace/marketplacehub

# Install cross-cutting to local maven repository
WORKDIR /workspace/cross-cutting
RUN /workspace/marketplacehub/backend/mvnw clean install -DskipTests

# Build the application skipping tests
WORKDIR /workspace/marketplacehub/backend
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the specific jar from builder
COPY --from=builder /workspace/marketplacehub/backend/target/marketplacehub-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8108

ENTRYPOINT ["java", "-jar", "app.jar"]
