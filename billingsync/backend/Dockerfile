FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app
# First build cc-starter
COPY pom.xml .
COPY ../cross-cutting/cc-starter ../cross-cutting/cc-starter
COPY ../cross-cutting/pom.xml ../cross-cutting/pom.xml
# Note: we assume cc-starter is already installed in local maven cache or built along the way

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8106
ENTRYPOINT ["java", "-jar", "app.jar"]
