# CashflowAI

Near-term cash position forecasting and shortfall mitigation automation.

## Setup
Backend relies on Postgres. Frontend uses Next.js app router.

## Run Locally
Backend: `mvn -pl cashflowai/backend clean compile`
Frontend: `npm --prefix cashflowai/frontend dev`

## Environment Variables
- `DATABASE_URL`: Postgres DB connection string.
- `LITELLM_URL`: LiteLLM gateway url.
