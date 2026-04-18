# Handoff Notes - AuditVault

## Questions Resolved During Build
- Q: Do we actually implement real document generation for audit packages?
  A: Currently implemented as dummy URL link for synchronous/fast resolution, but records to DB. Real generation logic would need a PDF engine like JasperReports or openhtmltopdf.

## Assumptions
- LiteLLM gateway connects properly locally to dummy API keys or OpenAI mapping keys.
- H2 is used for testing environments to avoid context loading failures from testcontainers on Postgres.
- `cc-starter` module handles tenant header resolution via `X-Tenant-ID`.

## Future Work
- [ ] Implement robust event consumers using pgmq/queues to ingest from apps organically rather than purely REST `POST /evidence`.
- [ ] Real PDF or ZIP package generation logic.
- [ ] E2E tests using Cypress or Playwright.

## Integration notes
- Need to configure webhooks/events across the hub so other apps correctly emit `deploy.completed` and others mapped in `consumes`.
