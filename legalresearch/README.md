# LegalResearch

## Overview
Legal intelligence micro-saas focusing on citation tracking, precedent ranking, and memo generation.

## Setup
1. Backend requires Postgres.
2. Ensure you run `mvn install` in the `cross-cutting` directory before building.
3. The frontend is Next.js built and tested with npm.

## Integration Contracts
See `integration-manifest.json` for details on consumed and emitted events.

## Build and Run
- Backend: `mvn -pl legalresearch/backend clean verify`
- Frontend: `npm --prefix legalresearch/frontend test`

## Environment
- DB_URL
- DB_USER
- DB_PASS
- LITELLM_URL
