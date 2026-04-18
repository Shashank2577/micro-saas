# UsageIntelligence

UsageIntelligence is an AI-powered product analytics platform for the micro-SaaS ecosystem. It tracks feature adoption, user journeys, activation funnels, and feature flag impacts, and surfaces AI-driven insights to explain usage patterns.

## Features
- **Event Tracking:** Track arbitrary user events with JSON properties.
- **Metric Aggregation:** Generate MAU/DAU metrics and view trends.
- **Cohorts:** Define and manage user segments using JSON criteria.
- **AI Insights:** Generate automated recommendations and anomaly detection based on recent usage data using Claude/LiteLLM.

## Setup

### Backend (Spring Boot)
1. Ensure PostgreSQL is running.
2. Run migrations via Flyway on startup.

### Frontend (Next.js)
1. Install dependencies:
   npm install

## Integration
This service uses Keycloak for authentication and LitLLM for AI features. See `integration-manifest.json` for events emitted and consumed by this service.

## Testing
Run backend tests: `mvn clean verify`
Run frontend tests: `npm test`
