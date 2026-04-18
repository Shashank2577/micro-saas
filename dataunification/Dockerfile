FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY backend/.mvn/ .mvn
COPY backend/mvnw backend/pom.xml ./
RUN ./mvnw dependency:go-offline
COPY backend/src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8170
ENTRYPOINT ["java", "-jar", "app.jar"]
