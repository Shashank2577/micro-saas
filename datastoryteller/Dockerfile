FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY backend/pom.xml backend/
RUN mvn -pl backend dependency:go-offline

COPY backend/src backend/src
RUN mvn -pl backend clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/backend/target/datastoryteller-*.jar app.jar
EXPOSE 8150
ENTRYPOINT ["java", "-jar", "app.jar"]
