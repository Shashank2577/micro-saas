# InsightEngine

InsightEngine is an AI-powered insight discovery and alerting platform that automatically surfaces important patterns, anomalies, and recommendations from data.

## Features
- Continuous insight discovery
- Anomaly detection
- Correlation discovery
- Insight ranking
- Contextual AI explanations
- Recommended actions
- Alert workflows

## Setup

### Backend (Spring Boot 3.3.5)
The backend requires PostgreSQL.
1. Run `docker compose up -d` in `../infra` to start dependencies.
2. Run `mvn clean verify` in `insightengine/backend`.
3. Run `java -jar target/insightengine-0.0.1-SNAPSHOT.jar`.

### Frontend (Next.js)
1. Run `npm install` in `insightengine/frontend`.
2. Run `npm run dev` to start the frontend on port 3000.

## Environment Variables
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` for PostgreSQL.
- `NEXT_PUBLIC_API_URL` for the frontend to connect to the backend.

## OpenAPI Documentation
Swagger UI is available at `http://localhost:8171/swagger-ui.html` when the backend is running.
