# EcosystemMap Handoff Notes

## Completeness
- Backend is completed with 8 entities, 7 services, and 5 controllers.
- Integrated LiteLLM using a local implementation for the AiInsightService.
- Flyway migrations are established.
- Frontend includes basic CRUD views in Next.js.
- Docker configuration (`Dockerfile`, `docker-compose.yml`) is ready.

## Trade-offs and Assumptions
- For background event processing, we are using `TenantContext.executeWithTenant` in order to correctly set the tenant identity from webhook payloads before storing.
- As the standard `LiteLlmClient` wasn't exposed perfectly from the `cc-starter` module in the environment context, a local implementation of the `LiteLlmClient` was placed in the service package to bypass the compilation failure while maintaining the AI integration feature.

## Required Follow-ups
- Hook the Next.js pages up with an actual authentication and user tenant provider. The current `api.ts` makes a request to `/api/*` assuming tenant headers are either bypassed or implicitly forwarded.
- Real API testing with the live `litellm` orchestration container to verify AI prompt processing quality.
