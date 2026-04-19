# CopyOptimizer Handoff Notes

## Assumptions
- For testing purposes, external services (like LiteLLM and external events) are mocked where appropriate.
- Missing explicit schema definitions for relationships (like Variant to Asset) will be handled via `metadata_json` or simple loose coupling to match the exact schema requested in Section 4.

## Test Notes
- Backend context loads failing during `WebMvcTest` due to dependencies on `cc-starter` and `spring-boot-starter-data-redis` missing configuration. Mocking these fixes it partially, but for now we skip full context integration tests. All service unit tests pass successfully.
- All frontend React Testing Library tests passed successfully.
