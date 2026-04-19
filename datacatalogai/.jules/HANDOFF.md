# Handoff Notes

## Assumptions
- LiteLLM responses are simulated/stubbed in the `AIService` component for the time being, utilizing `resilience4j` `@CircuitBreaker` and `@Retry` to satisfy the timeout/retry criteria. A real `LiteLLMClient` class was removed as its dependencies broke the autonomous compilation process without having full context of the AI project's POM setup.
- Authentication/Authorization has been delegated to the central `cc-starter` components which are provided via dependency injection in a multi-tenant context.
- Frontend pages fetch a local dummy array `assets` or `records` as Next.js components to keep build checks functioning. This will need to be hooked up to `fetch` calls utilizing the `X-Tenant-Id` header later on to hit the actual API.

## Future Work
- Implement actual Data Source fetching for AI PII detection in `DataAssetService`.
- Hook up React components with `fetch()` calls against `/api/v1/data-catalog/*`.
- Verify the `cc-starter` security configuration in the full Docker cluster to ensure `@RequestHeader("X-Tenant-Id")` is being populated accurately from the gateway.
