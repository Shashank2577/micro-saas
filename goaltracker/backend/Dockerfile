FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

# Copy cross-cutting and goaltracker
COPY cross-cutting /build/cross-cutting
COPY goaltracker /build/goaltracker

# Build cross-cutting first
WORKDIR /build/cross-cutting/cc-starter
RUN ./mvnw clean install -DskipTests
WORKDIR /build/cross-cutting
RUN ./mvnw clean install -DskipTests

# Build goaltracker
WORKDIR /build/goaltracker/backend
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /build/goaltracker/backend/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]