# CreatorAnalytics

CreatorAnalytics provides content performance attribution and ROI insights for creator teams.

## Tech Stack
- **Backend**: Java 21, Spring Boot 3.2, PostgreSQL, Flyway
- **Frontend**: Next.js 14, React, TailwindCSS

## Local Development

### Backend
1. Ensure PostgreSQL is running.
2. Setup database: `createdb creatoranalytics`
3. Run backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
4. Access API docs at `http://localhost:8080/swagger-ui.html`

### Frontend
1. Install dependencies: `cd frontend && npm install`
2. Run dev server: `npm run dev`
3. Access UI at `http://localhost:3000/creator-analytics`

## Testing
- Backend tests: `cd backend && mvn test`
- Frontend tests: `cd frontend && npm test`
