# PerformanceNarrative

## Overview
PerformanceNarrative is an HR Talent app for performance review narrative drafting with calibration support.
It is part of the micro-saas ecosystem.

## Setup
Backend runs on port 8093. Requires Java 21 and Maven.
Frontend runs Next.js. Requires Node 20+.

## Environmental Variables
- `PORT`: 8093
- `JDBC_DATABASE_URL`: PostgreSQL URL
- `LITELLM_URL`: LiteLLM server URL (e.g. `http://localhost:4000`)
- `LITELLM_API_KEY`: Key for LiteLLM

## Runbook
- Start dependencies: `docker-compose up -d postgres`
- Backend: `mvn -pl performancenarrative/backend spring-boot:run`
- Frontend: `cd performancenarrative/frontend && npm run dev`
