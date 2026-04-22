# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy the cross-cutting modules needed for the build
COPY cross-cutting/ /cross-cutting/
RUN cd /cross-cutting && ./mvnw clean install -DskipTests

# Copy the app source
COPY invoiceprocessor/backend/ /app/
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8114
ENTRYPOINT ["java", "-jar", "app.jar"]
