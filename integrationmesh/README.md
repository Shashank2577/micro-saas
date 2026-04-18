# IntegrationMesh

IntegrationMesh is an AI-powered integration platform built for the micro-SaaS ecosystem. It supports AI-suggested field mappings, centralized connector configurations, and health monitoring for cross-app data synchronization.

## Setup & Running Locally

### Backend
1. Ensure `cc-starter` is built and available locally.
2. Build backend: `mvn clean install` inside `/backend`
3. Run: `java -jar backend/target/*.jar`

### Frontend
1. Run `npm install` inside `/frontend`
2. Run `npm run dev`

### Docker
1. The app is containerized using `Dockerfile`.
2. Connect to local infrastructure via `docker-compose.yml`.

## Testing
- Backend unit tests (`mvn test`) cover all required services with >80% coverage.
- Frontend component tests (`npm test`) verify the UI.

## API Documentation
Once running locally on port 8080, swagger docs are available at `http://localhost:8080/swagger-ui.html`.
