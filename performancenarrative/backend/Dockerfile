FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build

COPY cross-cutting /build/cross-cutting
RUN cd cross-cutting && ./mvnw install -DskipTests -q

COPY performancenarrative/backend/pom.xml .
COPY performancenarrative/backend/src ./src

RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8093

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8093/actuator/health || return 1

ENTRYPOINT ["java", "-jar", "app.jar"]
