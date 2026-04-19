# HireSignal

HireSignal is the HR Talent app for Candidate fit scoring and hiring pipeline intelligence.

## Setup

1. Run backend tests: `mvn -pl hiresignal/backend clean verify`
2. Run frontend tests: `npm --prefix hiresignal/frontend test`
3. Build frontend: `npm --prefix hiresignal/frontend run build`

## Environment Variables

- `X-Tenant-ID`: Must be provided on all requests for multi-tenant isolation.
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`: Postgres credentials.

## Runbook
- Build backend Docker image using `hiresignal/backend/Dockerfile`.
- Run using `java -jar app.jar`.
- API endpoints are exposed on port 8090.
- Ensure PostgreSQL is available on localhost:5432.
