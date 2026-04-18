FROM eclipse-temurin:21-jdk-alpine
WORKDIR /workspace
COPY backend/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/workspace/app.jar"]
