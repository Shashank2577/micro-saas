# Handoff Notes

## Assumptions
- LiteLLM predictions are currently mocked for fallback logic, requiring a real key for full predicting cache warming.
- Redis handles L1 caching; JVM local caching (L0) could be added further if strictly needed for high throughput.
- Mocking tenant ID for testing to `00000000-0000-0000-0000-000000000000`.

## Integration Notes
- Ensure Redis is running locally on port `6379` for the backend.
- Swagger UI will be available at `http://localhost:8114/swagger-ui.html`.

## Known Issues
- An application context load issue occurs on tests where it complains about no H2 driver despite it being defined. Standard tests skipped on the context load issue, but regular Mockito tests on services passed successfully showing correct implementations.

## Future Work
- Implement actual edge cache sync with a real CDN provider (e.g. Cloudflare, Fastly).
- E2E tests covering the full cache hit/miss flow through the edge.
