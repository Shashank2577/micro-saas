FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace/app
COPY backend/pom.xml .
COPY backend/src src
RUN apt-get update && apt-get install -y maven
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target
COPY --from=build ${DEPENDENCY}/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
