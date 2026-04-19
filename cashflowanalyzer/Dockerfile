FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY backend/target/*.jar app.jar
EXPOSE 8206
ENTRYPOINT ["java", "-jar", "app.jar"]
