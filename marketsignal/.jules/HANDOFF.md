# Handoff Notes

## Future Work
- Actual integration with external APIs (TechCrunch, WIPO, USPTO, LinkedIn, etc.) is simulated or requires external job orchestration since we don't have API keys or robust web scrapers built-in.
- Advanced semantic clustering could be improved with better embedding models.
- The UI visualization of trends needs more sophisticated charting (e.g., using D3.js or advanced Recharts).

## Assumptions
- LiteLLM is available at the standard infrastructure endpoint.
- PgVector is installed on the PostgreSQL instance.

## Infrastructure
- Add the following to `infra/compose.apps.yml` to run:
```yaml
  marketsignal-backend:
    build: ../marketsignal/backend
    ports:
      - "8149:8149"
    environment:
      - DB_HOST=postgres
      - DB_USER=postgres
      - DB_PASSWORD=postgres
      - DB_NAME=marketsignal
      - LITELLM_BASE_URL=http://litellm:4000
    depends_on:
      - postgres
      - litellm
```
