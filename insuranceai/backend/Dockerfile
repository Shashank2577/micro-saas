FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Install cc-starter locally first since it's a shared local dependency
COPY ../cross-cutting /cross-cutting
RUN mvn -f /cross-cutting/pom.xml install -DskipTests
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
