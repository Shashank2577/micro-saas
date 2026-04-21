FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app
COPY ../cross-cutting /app/cross-cutting
COPY . /app/brandvoice/backend
WORKDIR /app/brandvoice/backend

RUN chmod +x ./mvnw || true
RUN mvn clean package -DskipTests || ../../cross-cutting/mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/brandvoice/backend/target/*.jar app.jar
EXPOSE 8131
ENTRYPOINT ["java", "-jar", "app.jar"]
