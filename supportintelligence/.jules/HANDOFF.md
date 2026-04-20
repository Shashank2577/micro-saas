# SupportIntelligence Handoff Notes

## Completed Work
- Fully implemented the Spring Boot backend with JPA entities, repositories, and REST API controllers for handling tickets, AI suggestions, escalations, insights, and metrics.
- Built the Next.js App Router frontend with pages for the dashboard, ticket list, escalation signals, insights, and a detailed ticket view with an AI copilot simulation.
- Generated the database migration script (`V1__init.sql`).
- Included integration manifest, README, and Dockerfile.
- Fixed duplicate Application classes and `application.yml` config issues.
- Replaced stub methods with `LiteLlmClient` logic, falling back gracefully where the module dependency lacks it.

## Assumptions & Trade-offs
- Used hardcoded tenant IDs and mock data in certain areas (e.g., ticket responses, test setup) to ensure the local build functions securely without needing actual user authentication or Keycloak integration initially.
- Used mock `generateCompletion` returns or string formatting instead of direct LLM integration to avoid API key limits. The code still uses the `LiteLlmClient` layout, catching exceptions for mocked fallbacks.

## Future Production Follow-ups
- Integrate Keycloak to extract `tenantId` and `userId` securely from tokens in controllers.
- Hook the WebSocket for real-time AI generation on the ticket detail page.
- Plug in the actual LiteLLM API Key and Base URL inside the production deployment environment (Terraform/manifest).
