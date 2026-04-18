# ProcessMiner

ProcessMiner is an AI business process intelligence platform. Analyzes system event logs to reconstruct actual process flows (not documented ones). Identifies bottlenecks, loops, and rework patterns. Finds compliance gaps where processes deviate from policy. Surfaces highest-ROI automation opportunities with effort estimates.

## Services
- Backend: Spring Boot 3.3.5 (Port 8155)
- Frontend: Next.js (Port 3155 via Docker / 3000 Local)
- Infrastructure: PostgreSQL, Redis, LiteLLM

## Setup
Local infrastructure is managed via Docker Compose in the root `infra/` folder.
To start this app alongside the infrastructure:
```bash
docker compose -f infra/compose.yml -f infra/compose.apps.yml up -d
```

## Running Locally (without Docker for apps)
- **Backend:** `cd processminer/backend && mvn spring-boot:run`
- **Frontend:** `cd processminer/frontend && npm run dev`

## Testing
- Backend tests: `cd processminer/backend && mvn clean verify`
- Frontend tests: `cd processminer/frontend && npm run test`
