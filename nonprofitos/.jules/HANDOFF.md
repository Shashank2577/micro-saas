# Handoff Notes

## Questions Resolved
- Q: What are the primary entities? A: Based on the "donor intelligence", "grant writing", and "impact measurement" features from the design doc, I created Donors, Grants, and Impacts.

## Assumptions
- AI operations will be synchronous but protected by circuit breakers.
- We will mock Keycloak / JWT for local testing if needed.

## Future Work
- Integration with real donor databases (e.g., Raiser's Edge).

## Phase 2 Notes
- Q: Do we need a real AiService implementation talking to LiteLLM? A: For the sake of isolated, deterministic testing and reducing external dependencies during initial development, I've implemented a local mock in `AiService` that returns predictable strings. In a production environment, this should be swapped with a real LiteLLM client (e.g. using Spring AI or direct HTTP calls to the gateway).
- Assumptions:
  - The `cc-starter` correctly sets the `TenantContext` from the `X-Tenant-ID` header using a filter.
  - The Keycloak setup in `infra/` works as intended and validates tokens. For local testing without Keycloak, the `@CrossOrigin` or security configurations may need adjustment.
- Future work:
  - Implement full E2E testing with Cypress or Playwright.
  - Wire the frontend to a real Keycloak instance for auth.
