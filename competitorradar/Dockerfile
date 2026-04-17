FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build

COPY pom.xml ./
COPY .mvn .mvn/
COPY mvnw ./
COPY mvnw.cmd ./

COPY cross-cutting/ cross-cutting/
RUN cd cross-cutting && ../mvnw clean install -DskipTests

COPY competitorradar/ competitorradar/
RUN cd competitorradar/backend && ../../mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/competitorradar/backend/target/*.jar app.jar
EXPOSE 8144
ENTRYPOINT ["java", "-jar", "app.jar"]
