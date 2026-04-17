FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY backend/target/backupvault-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8117
ENTRYPOINT ["java", "-jar", "app.jar"]
