FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY backend/pom.xml backend/
COPY frontend/package*.json frontend/

# We'll use multi-module build from root for this
COPY cross-cutting cross-cutting
COPY insightengine insightengine
RUN ./mvnw -pl insightengine/backend clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/insightengine/backend/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
