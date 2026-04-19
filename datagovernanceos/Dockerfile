FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY backend/target/datagovernanceos-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8283
ENTRYPOINT ["java", "-jar", "app.jar"]
