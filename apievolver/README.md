# APIEvolver — API contract management + backward compatibility checker

> Part of the [micro-saas ecosystem](../README.md) | **Cluster A** | Port 8094

## What It Does

APIEvolver prevents breaking API changes from reaching consumers by automatically diffing OpenAPI specs between versions and classifying every change as safe or breaking. When a new spec is uploaded it notifies all registered downstream consumers of incompatible changes before deployment, giving teams a contract-first workflow that prevents integration failures across service boundaries.

## AI Pattern

Agent — spec diffing pipeline with AI-assisted change classification that distinguishes semantic breaking changes from safe additions.

## Stack

- **Backend:** Spring Boot 3.3.5 + cc-starter 0.1.0-SNAPSHOT (port 8094)
- **Database:** PostgreSQL (schema: `apievolver`)
- **Frontend:** Next.js 14 + TypeScript + Tailwind

## Key Entities

- **ApiSpec** — a versioned OpenAPI specification document for a service
- **SpecDiff** — computed difference between two spec versions
- **BreakingChange** — a specific incompatible change with severity and description
- **Consumer** — a registered downstream service that depends on an API contract
- **Notification** — an alert sent to a consumer about an impending breaking change

## API Endpoints

```
POST   /api/specs                          Upload a new API spec version
GET    /api/specs/{serviceId}              List spec versions for a service
POST   /api/specs/diff                     Diff two specs (input: oldSpec + newSpec)
GET    /api/specs/diff/{diffId}            Get diff report
POST   /api/specs/diff/{diffId}/breaking   Detect breaking changes from a diff
POST   /api/consumers                      Register a downstream consumer
POST   /api/notifications/send             Notify consumers of a change
GET    /actuator/health                    Health check
```

## Running Locally

```bash
cd apievolver/backend && mvn spring-boot:run
# API: http://localhost:8094
```

## Integration

Emits and consumes events through [Nexus Hub](../nexus-hub/) (port 8090).

**Emits:** `api-spec-uploaded`, `breaking-change-detected`, `api-deprecated`
**Consumes:** `service.registered`, `api-spec-scanned`

See [integration-manifest.json](integration-manifest.json) for the full event schema.
