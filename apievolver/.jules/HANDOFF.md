# Handoff Notes

## Assumptions
- Replaced the existing old models with the new UUID/tenant_id domain model since this is a clean break/evolution.
- `SpecDiffService` logic was removed and mocked/reimplemented minimally since it belonged to the old schema.
- Frontend dashboard is replaced entirely by the new page layout matching the spec components.
- The `AiAnalysisService` mocks a LiteLLM call using a circuit breaker as the exact integration isn't provided here.
