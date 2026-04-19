# APIEvolver — Next-15 Production Progression Spec

## Product Intent
- App: `apievolver`
- Domain: Developer Platform
- Outcome: API contract lifecycle management and backward-compatibility analysis
- Primary actors: Platform engineer, API owner, Developer advocate

## Current Snapshot
- Score: 60
- Tier: Beta
- Depth: Shallow
- Risks: no-frontend-tests, no-backend-dockerfile
- Low-hanging fruits:
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.

## Scope (must be complete in one Jules session)
- [ ] 1. specs: backend service + API + frontend page + tests
- [ ] 2. versions: backend service + API + frontend page + tests
- [ ] 3. diffs: backend service + API + frontend page + tests
- [ ] 4. compatibility: backend service + API + frontend page + tests
- [ ] 5. deprecations: backend service + API + frontend page + tests
- [ ] 6. sdk: backend service + API + frontend page + tests

## Domain Model
### ApiSpec
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ApiSpec display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ApiVersion
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ApiVersion display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### BreakingChange
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | BreakingChange display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### CompatibilityReport
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | CompatibilityReport display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### DeprecationNotice
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | DeprecationNotice display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### SdkArtifact
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SdkArtifact display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/api-evolution/api-specs
- POST /api/v1/api-evolution/api-specs
- GET /api/v1/api-evolution/api-specs/{id}
- PATCH /api/v1/api-evolution/api-specs/{id}
- POST /api/v1/api-evolution/api-specs/{id}/validate
- GET /api/v1/api-evolution/api-versions
- POST /api/v1/api-evolution/api-versions
- GET /api/v1/api-evolution/api-versions/{id}
- PATCH /api/v1/api-evolution/api-versions/{id}
- POST /api/v1/api-evolution/api-versions/{id}/validate
- GET /api/v1/api-evolution/breaking-changes
- POST /api/v1/api-evolution/breaking-changes
- GET /api/v1/api-evolution/breaking-changes/{id}
- PATCH /api/v1/api-evolution/breaking-changes/{id}
- POST /api/v1/api-evolution/breaking-changes/{id}/validate
- GET /api/v1/api-evolution/compatibility-reports
- POST /api/v1/api-evolution/compatibility-reports
- GET /api/v1/api-evolution/compatibility-reports/{id}
- PATCH /api/v1/api-evolution/compatibility-reports/{id}
- POST /api/v1/api-evolution/compatibility-reports/{id}/validate
- GET /api/v1/api-evolution/deprecation-notices
- POST /api/v1/api-evolution/deprecation-notices
- GET /api/v1/api-evolution/deprecation-notices/{id}
- PATCH /api/v1/api-evolution/deprecation-notices/{id}
- POST /api/v1/api-evolution/deprecation-notices/{id}/validate
- POST /api/v1/api-evolution/ai/analyze
- POST /api/v1/api-evolution/workflows/execute
- GET /api/v1/api-evolution/metrics/summary

## Event Contract
- Emits: apievolver.breaking-change.detected, apievolver.version.published, apievolver.sdk.generated
- Consumes: apimanager.version.published, dependencyradar.scan.completed, securitypulse.finding.created
- All events include: `event_id`, `tenant_id`, `occurred_at`, `source_app`, `payload`
- Consumers are idempotent by `event_id`

## Non-Functional Requirements
- P95 latency target: <300ms reads, <600ms writes
- Multi-tenant isolation on all reads/writes
- Structured logs with correlation IDs
- OpenAPI completeness and contract tests

## Acceptance Criteria
1. All module slices implemented with API + UI + tests.
2. Strict tenant isolation and RBAC checks on all endpoints.
3. Event emit/consume contracts implemented and validated.
4. OpenAPI includes all endpoints from this spec.
5. Backend and frontend test suites pass in CI.
6. LiteLLM calls are guarded with timeout/retry/circuit-breaker.
7. No TODO or stubbed production logic remains.

## Build & Verify
- `mvn -pl apievolver/backend clean verify`
- `npm --prefix apievolver/frontend test`
- `npm --prefix apievolver/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `apievolver/.jules/HANDOFF.md`
- Submit PR when done
