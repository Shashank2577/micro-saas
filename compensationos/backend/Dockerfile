FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY cross-cutting /app/cross-cutting
COPY compensationos /app/compensationos
WORKDIR /app/compensationos/backend
RUN ../../cross-cutting/mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/compensationos/backend/target/compensationos-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
