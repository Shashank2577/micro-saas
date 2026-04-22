# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace/app

# Copy the entire parent structure if needed, or just the backend.
# Assuming standard single-module build for this app inside docker.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Run build
RUN ./mvnw install -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
