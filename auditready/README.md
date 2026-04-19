# AuditReady

Audit preparation and compliance readiness platform. Automate evidence collection, track compliance gaps, generate audit reports, and manage remediation workflows.

## Features

- Control framework mapping
- Evidence management
- Gap analysis
- Audit trails
- Audit reporting

## Tech Stack

- **Backend:** Spring Boot 3.3.5 (Java 21), PostgreSQL, Flyway
- **Frontend:** Next.js (App Router), React, Tailwind CSS

## Running Locally

1. Start dependencies: `docker-compose up -d postgres`
2. Start backend: `cd backend && mvn spring-boot:run`
3. Start frontend: `cd frontend && npm run dev`

## Testing

- Backend tests: `cd backend && mvn test`
- Frontend tests: `cd frontend && npm test`
