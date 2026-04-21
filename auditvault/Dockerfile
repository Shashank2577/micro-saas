FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY cross-cutting /app/cross-cutting
WORKDIR /app/cross-cutting
RUN mvn clean install -DskipTests

WORKDIR /app/auditvault/backend
COPY auditvault/backend /app/auditvault/backend
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /workspace
COPY --from=builder /app/auditvault/backend/target/auditvault-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/workspace/app.jar"]
