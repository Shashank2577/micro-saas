# CacheOptimizer

CacheOptimizer is a distributed caching and CDN service that manages cache policies, invalidation, edge distribution, and cache-aside patterns for all microservices in the ecosystem.

## Features
- Multi-layer caching (Redis + JVM)
- Configurable cache policies (TTL, compression, strategies)
- Analytics tracking (hit/miss ratios)
- Cache warming based on LiteLLM predictions
- Stale-while-revalidate strategies

## Backend
Built with Spring Boot 3.3.5 and Java 21.
- `mvn clean verify` to build and test
- Uses PostgreSQL for state and Redis for L1 cache
- Exposes OpenAPI documentation at `/swagger-ui.html`

## Frontend
Built with Next.js (App Router), TypeScript, and Tailwind.
- `npm run dev` to start locally
- Provides a dashboard for analytics and policy management
- `npm run test` for component tests (Vitest + Testing Library)

## Infrastructure
Run via Docker Compose in the `infra/` folder.
