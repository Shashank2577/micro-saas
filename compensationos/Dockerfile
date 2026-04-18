FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY backend/target/*.jar app.jar
EXPOSE 8147
ENTRYPOINT ["java", "-jar", "app.jar"]
