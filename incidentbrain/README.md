# IncidentBrain — AI root cause analysis from Datadog + GitHub signals

> Part of the [micro-saas ecosystem](../README.md) | **Cluster A** | Port 8091

## What It Does

IncidentBrain reduces mean-time-to-resolution by correlating signals from Datadog alerts, GitHub commits, and deployment history to automatically identify the root cause of production incidents. It ingests raw observability data, runs AI-powered correlation analysis, and surfaces a ranked list of probable causes with supporting evidence so on-call engineers spend minutes diagnosing instead of hours.

## AI Pattern

Agent — autonomous multi-signal correlation pipeline that reasons over time-series alerts, recent code changes, and deployment events to produce a root-cause hypothesis.

## Stack

- **Backend:** Spring Boot 3.3.5 + cc-starter 0.1.0-SNAPSHOT (port 8091)
- **Database:** PostgreSQL (schema: `incidentbrain`)
- **Frontend:** Next.js 14 + TypeScript + Tailwind

## Key Entities

- **Incident** — a production event with severity, timeline, and status
- **Signal** — raw data point ingested from Datadog, GitHub, or deployment systems
- **RootCause** — AI-generated hypothesis linking signals to a probable cause
- **Timeline** — ordered sequence of signals and events for a given incident
- **Playbook** — runbook template linked to a root cause pattern

## API Endpoints

```
POST   /api/incidents                  Create a new incident
GET    /api/incidents/{id}             Get incident details
POST   /api/incidents/{id}/analyze    Trigger root cause analysis
GET    /api/incidents/{id}/root-causes List ranked root cause hypotheses
POST   /api/signals                    Ingest a raw signal
GET    /actuator/health                Health check
```

## Running Locally

```bash
cd incidentbrain/backend && mvn spring-boot:run
# API: http://localhost:8091
```

## Integration

Emits and consumes events through [Nexus Hub](../nexus-hub/) (port 8090).
See [integration-manifest.json](integration-manifest.json) for the full event schema.
