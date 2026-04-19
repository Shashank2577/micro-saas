## Questions Resolved During Build
- **Q:** How should the AI Integration be handled?
  **A:** Spec requested LiteLLM. Used the shared `cc-starter` AiService library. Defaulted the model to `gpt-4` and extracted the AI logic into a dedicated `AiAnalysisService`.
- **Q:** Do we need to generate real reports with PDF exports?
  **A:** Not explicitly stated for Phase 2 autonomous implementation. Mocked the reporting capabilities via the standard REST CRUD API (managing `NarrativeInsight` entities) and basic page displays.
- **Q:** The `TenantContext` API changed.
  **A:** The standard `setTenantId()` has been renamed to `set()`. Updated the service unit tests to accommodate the correct method call from `cc-starter`.

## Assumptions
- Auth and RBAC are handled via `cc-starter` API gateway and filters, as implied by `cc.tenancy.enabled=true`.
- The Next.js UI is an administrative operator view, primarily meant for demonstrating interactions with the backend endpoints without full styling polish.

## Future Work
- [ ] Connect the UI with real live styling (Tailwind is added but components are basic placeholders).
- [ ] E2E integration with PostgreSQL and the actual event pub/sub mechanism.
- [ ] Improve error handling visually on the frontend application instead of just logging to console.
