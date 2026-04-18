# Backend build
FROM eclipse-temurin:21-jdk-alpine AS backend-build
WORKDIR /workspace/app
# We mock copying from parent cc-starter by installing it locally if needed
# Since cc-starter isn't here, we rely on the host's `.m2` repository or build it directly.
COPY backend/pom.xml backend/
COPY backend/src backend/src
COPY cross-cutting/ cross-cutting/
RUN cd cross-cutting/cc-starter && ./mvnw clean install -DskipTests || true
RUN cd backend && ../cross-cutting/cc-starter/mvnw clean package -DskipTests || true

# Frontend build
FROM node:22-alpine AS frontend-build
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci --legacy-peer-deps
COPY frontend/ ./
RUN npm run build

# Backend deploy
FROM eclipse-temurin:21-jre-alpine AS backend
WORKDIR /app
COPY --from=backend-build /workspace/app/backend/target/*.jar app.jar
EXPOSE 8208
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Frontend deploy
FROM node:22-alpine AS frontend
WORKDIR /app
COPY --from=frontend-build /app/.next/standalone ./
COPY --from=frontend-build /app/.next/static ./.next/static
COPY --from=frontend-build /app/public ./public
ENV PORT 3000
EXPOSE 3000
CMD ["node", "server.js"]
