# BillingAI

Subscription billing orchestration, dunning, and revenue leakage prevention for Revenue Operations.

## Setup

1. Requires Java 21+ and Node.js 20+.
2. Database: PostgreSQL 15+.
3. Start dependencies (e.g., using docker-compose).

## Environment Variables

- `DB_URL`: JDBC URL for PostgreSQL
- `DB_USER`: Database user
- `DB_PASS`: Database password
- `LITELLM_API_KEY`: API key for AI features

## Runbook

- Start Backend: `mvn -pl billingai/backend spring-boot:run`
- Start Frontend: `npm --prefix billingai/frontend run dev`
- Run Backend Tests: `mvn -pl billingai/backend clean verify`
- Run Frontend Tests: `npm --prefix billingai/frontend test`

## Docker

A Dockerfile is provided in `backend/Dockerfile`. It uses `eclipse-temurin:21-jre-alpine`.
