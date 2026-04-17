# Build backend
FROM maven:3.9.6-eclipse-temurin-21-jammy AS backend-build
WORKDIR /app
COPY pom.xml .
COPY backend/pom.xml backend/
RUN mvn dependency:go-offline -f backend/pom.xml
COPY backend/src backend/src
RUN mvn package -f backend/pom.xml -DskipTests

# Build frontend
FROM node:20-alpine AS frontend-build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Final image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=backend-build /app/backend/target/experimentengine-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8154
ENTRYPOINT ["java", "-jar", "app.jar"]
