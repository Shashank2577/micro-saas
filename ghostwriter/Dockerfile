# Backend build
FROM maven:3.9.6-eclipse-temurin-21-jammy AS backend-build
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src ./src
RUN mvn clean package -DskipTests

# Frontend build
FROM node:20-alpine AS frontend-build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .
RUN npm run build

# Combine
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy backend
COPY --from=backend-build /app/target/*.jar app.jar

# Copy frontend standalone build
COPY --from=frontend-build /app/.next/standalone ./frontend
COPY --from=frontend-build /app/.next/static ./frontend/.next/static
COPY --from=frontend-build /app/public ./frontend/public

# Setup startup script
RUN echo '#!/bin/bash\n\
java -jar app.jar &\n\
cd frontend && node server.js\n\
' > start.sh
RUN chmod +x start.sh

EXPOSE 8080 3000
CMD ["./start.sh"]
