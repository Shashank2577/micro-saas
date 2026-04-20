FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
# Copy cc-starter source
COPY cross-cutting /cross-cutting
COPY revopsai/backend /app/revopsai/backend
RUN cd /app/revopsai/backend && ../mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/revopsai/backend/target/*.jar app.jar
EXPOSE 8147
ENTRYPOINT ["java", "-jar", "app.jar"]
