# VoiceAgentBuilder

Voice AI agent platform. Build, deploy, and monitor voice agents for customer service, sales, and support using a visual builder.

## Running Locally

1. Setup DB `docker compose up -d postgres redis` in `infra/`
2. Backend: `cd backend && mvn spring-boot:run`
3. Frontend: `cd frontend && npm run dev`
