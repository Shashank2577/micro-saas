FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder

WORKDIR /app
COPY . .
RUN mvn -pl backend clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/backend/target/wealthplan-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8202
ENTRYPOINT ["java", "-jar", "app.jar"]
