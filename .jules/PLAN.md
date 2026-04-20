# Phase-Based Plan

1. **Phase 1: Spec Generation** (Completed)
2. **Phase 2: Project Scaffolding**
   - Create Next.js frontend scaffolding and Spring Boot backend scaffolding.
   - Use `cc-starter` parent for backend.
3. **Phase 3: Backend Implementation**
   - Create JPA entities, repositories, services, and controllers.
   - Implement NLP and LiteLLM integrations.
   - Write backend tests (`mvn clean test`).
4. **Phase 4: Frontend Implementation**
   - Implement React components and pages.
   - Write frontend tests (`vitest`).
5. **Phase 5: Integration Manifest & Verification**
   - Generate `integration-manifest.json`.
   - Run full verification (`mvn clean verify` and `npm test`).
6. **Phase 6: PR & Handoff**
   - Generate `HANDOFF.md` and commit the code.
