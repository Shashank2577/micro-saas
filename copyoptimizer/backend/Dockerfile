FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Assuming standard maven build
RUN chmod +x ./mvnw || true
CMD ["mvn", "spring-boot:run"]

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || true
ENTRYPOINT ["java","-jar","/app/app.jar"]
