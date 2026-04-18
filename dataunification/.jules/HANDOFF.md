# Handoff Notes
## Assumptions
- Next.js component testing is sufficient given the timeframe and requirements.
- LiteLLM integration suggests mappings in the backend by returning a mocked string as the current implementation doesn't have an active LLM running. Real LLM connection requires further network config for LiteLLM.
- Multi-tenancy logic relies on static header values from the Next.js `api.ts` file which uses X-Tenant-ID. In production, this would be retrieved from the logged-in user via Keycloak.

## Future Work
- Actual CDC configuration using Debezium to replicate DB changes.
- Integration tests against a running pgmq and PostgreSQL instance.
