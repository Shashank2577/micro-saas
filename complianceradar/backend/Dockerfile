FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8092
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8092/actuator/health || true
ENTRYPOINT ["java", "-jar", "app.jar"]
