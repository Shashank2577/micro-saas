FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests -pl cashflowai/backend

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/cashflowai/backend/target/*.jar app.jar
EXPOSE 8090
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -q --spider http://localhost:8090/actuator/health
ENTRYPOINT ["java", "-jar", "app.jar"]
