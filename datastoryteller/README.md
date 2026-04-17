# DataStoryTeller

**Tagline:** AI-powered data narrative and insight generation

DataStoryTeller connects to BI/warehouses and automatically produces structured markdown narratives describing what changed, what drove it, why it matters, and what to do next.

## Architecture
- **Backend:** Spring Boot 3.x (Java 21) running on port `8150`
- **Frontend:** Next.js 14 running on port `3150`
- **Database:** PostgreSQL (with Flyway migrations)
- **AI Integration:** LiteLLM proxy (Claude-sonnet-4-6) via Spring AI

## Ports
- Backend: `8150`
- Frontend: `3150` (When running standalone dev server)

## Environment Variables
- `SPRING_DATASOURCE_URL` - PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME` - PostgreSQL user
- `SPRING_DATASOURCE_PASSWORD` - PostgreSQL password
- `SPRING_AI_OPENAI_BASE_URL` - URL to LiteLLM instance (usually http://localhost:4000)

## Run Locally
1. Start infrastructure (Postgres, LiteLLM):
   `cd ../infra && docker-compose up -d`
2. Start backend:
   `cd backend && mvn spring-boot:run`
3. Start frontend:
   `cd frontend && npm install && npm run build && npm run start`

## Testing
Run backend tests:
`cd backend && mvn clean test`

Run frontend tests (if configured):
`cd frontend && npm test`

## Docker
To build and run in Docker:
`docker-compose -f ../infra/compose.f.yml up -d --build datastoryteller`
