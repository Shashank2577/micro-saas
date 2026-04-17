# DeploySignal — DORA metrics + deployment risk scoring

> Part of the [micro-saas ecosystem](../README.md) | **Cluster A** | Port 8093

## What It Does

DeploySignal tracks your four DORA metrics (deployment frequency, lead time for changes, change failure rate, mean time to restore) in real time and computes a pre-deployment risk score before each release goes out. By correlating deployment history with incident data it warns teams when a deployment pattern is likely to cause a failure, giving engineering leaders an objective view of delivery health.

## AI Pattern

Predictive ML — risk scoring model trained on historical deployment and incident correlations to forecast deployment failure probability.

## Stack

- **Backend:** Spring Boot 3.3.5 + cc-starter 0.1.0-SNAPSHOT (port 8093)
- **Database:** PostgreSQL (schema: `deploysignal`)
- **Frontend:** Next.js 14 + TypeScript + Tailwind

## Key Entities

- **Deployment** — a recorded release event with environment, version, and outcome
- **DORASnapshot** — point-in-time measurement of all four DORA metrics
- **RiskScore** — pre-deployment assessment with contributing risk factors
- **Pipeline** — a CI/CD pipeline tracked for frequency and lead time
- **ChangeFailure** — a deployment that resulted in a rollback or incident

## API Endpoints

```
POST   /api/deployments               Record a deployment event
GET    /api/deployments/{id}          Get deployment details
GET    /api/metrics/dora              Get current DORA metrics summary
GET    /api/metrics/dora/history      Get DORA metrics over time
POST   /api/risk-score                Compute risk score for a pending deployment
GET    /api/risk-score/{deploymentId} Get risk score for a recorded deployment
GET    /actuator/health               Health check
```

## Running Locally

```bash
cd deploysignal/backend && mvn spring-boot:run
# API: http://localhost:8093
```

## Integration

Emits and consumes events through [Nexus Hub](../nexus-hub/) (port 8090).
See [integration-manifest.json](integration-manifest.json) for the full event schema.
