# IdentityCore

IdentityCore is an AI identity intelligence platform designed to continuously analyze access patterns, detect anomalies, surface overprivileged accounts, and automate quarterly access reviews. It relies on a continuous analysis pipeline and an agentic model for automating access reviews.

## Setup

1. Run infrastructure dependencies via Docker Compose: `docker-compose up -d` in the `infra/` folder.
2. Build backend: `cd backend && mvn clean compile`
3. Run backend tests: `mvn test`
4. Build frontend: `cd frontend && npm install && npm run build`
5. Run frontend tests: `npx vitest run`

## Tech Stack
- Backend: Java 21, Spring Boot 3, Postgres
- Frontend: Next.js 14, React, Tailwind CSS, TypeScript
