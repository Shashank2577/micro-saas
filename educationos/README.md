# EducationOS

EducationOS is an AI learning platform builder. It generates personalized learning paths from curriculum goals and learner background, creates assessments and quizzes from content automatically, tracks learner progress with mastery detection, and adapts content difficulty.

## Setup & Running Locally

1. **Infrastructure**: Ensure local infrastructure (PostgreSQL, Redis, Keycloak) is running via `docker compose up -d` in the `/infra` directory.
2. **Backend**:
   - `cd backend`
   - `mvn spring-boot:run`
   - Runs on `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
3. **Frontend**:
   - `cd frontend`
   - `npm run dev`
   - Runs on `http://localhost:3000`

## Testing

- **Backend**: `cd backend && mvn clean verify`
- **Frontend**: `cd frontend && npm test`
