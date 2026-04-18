FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY backend/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
