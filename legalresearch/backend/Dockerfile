FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8091
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://localhost:8091/actuator/health || return 1
ENTRYPOINT ["java", "-jar", "app.jar"]
