# Handoff Notes

## Assumptions
- LiteLLM is hosted locally at http://localhost:4000/v1/chat/completions.
- PII encryption at rest is handled by PostgreSQL / Spring but explicitly not implemented as a Java `AttributeConverter` because we want straightforward models initially; standard DB-level encryption is assumed.
- The `tenant_id` header `X-Tenant-ID` is passed as a string/UUID in HTTP requests.
- Next.js fetches are done on the client-side for simplicity.

## Questions Resolved
- Q: Do we need full pgmq job workers for pulse surveys? A: Mocked synchronous behavior initially for speed, easily convertible to async pgmq processing.

## Future Work
- Add actual `pgmq` queue consumer implementations using `cc-starter` event handling.
- Expand PDF/CSV Generation with actual `itextpdf` / `commons-csv` builders.
