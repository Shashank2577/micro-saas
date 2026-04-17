# AgentOps

AI agent observability and governance platform. Captures agent runs, tool calls, costs, and manages human escalations.

## Tech Stack
- **Backend:** Spring Boot 3.3.5 (Java 21), PostgreSQL, Flyway
- **Frontend:** Next.js (App Router), TypeScript, Tailwind CSS
- **Testing:** JUnit, Mockito, Vitest, React Testing Library

## Setup & Running Locally

1. **Start Infrastructure:**
   From the repository root:
   `cd infra && docker-compose up -d postgres redis keycloak`

2. **Start Backend:**
   `cd agentops/backend && mvn spring-boot:run`

3. **Start Frontend:**
   `cd agentops/frontend && npm run dev`

## Documentation
- API Documentation (Swagger UI): `http://localhost:8080/swagger-ui.html`
- Autonomous build notes can be found in the `.jules/` directory.
