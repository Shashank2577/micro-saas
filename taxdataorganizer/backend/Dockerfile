FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY cross-cutting /app/cross-cutting
COPY taxdataorganizer /app/taxdataorganizer
WORKDIR /app/taxdataorganizer/backend
RUN ../../cross-cutting/mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/taxdataorganizer/backend/target/*.jar app.jar
EXPOSE 8118
ENTRYPOINT ["java", "-jar", "app.jar"]
