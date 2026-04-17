FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
