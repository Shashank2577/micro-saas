FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /app
COPY . .
RUN mvn -B -ntp clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8150
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8150/actuator/health || true
ENTRYPOINT ["java", "-jar", "app.jar"]
