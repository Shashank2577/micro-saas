FROM eclipse-temurin:21-jdk-alpine AS backend-build
WORKDIR /app
COPY ./cross-cutting /app/cross-cutting
COPY ./customerdiscoveryai/backend /app/backend
WORKDIR /app/cross-cutting
RUN ./mvnw clean install -DskipTests
WORKDIR /app/backend
RUN ./mvnw clean package -DskipTests

FROM node:20-alpine AS frontend-build
WORKDIR /app
COPY ./customerdiscoveryai/frontend /app/frontend
WORKDIR /app/frontend
RUN npm ci
RUN npm run build

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-build /app/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
