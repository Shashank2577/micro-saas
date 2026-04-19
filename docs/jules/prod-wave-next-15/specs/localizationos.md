# LocalizationOS — Next-15 Production Progression Spec

## Product Intent
- App: `localizationos`
- Domain: Creator & Content Operations
- Outcome: Localization workflow orchestration with translation memory and quality checks
- Primary actors: Localization manager, Content ops, Regional reviewer

## Current Snapshot
- Score: 55
- Tier: Alpha
- Depth: Shallow
- Risks: no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add backend service/controller tests for critical paths.
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. projects: backend service + API + frontend page + tests
- [ ] 2. jobs: backend service + API + frontend page + tests
- [ ] 3. memory: backend service + API + frontend page + tests
- [ ] 4. variants: backend service + API + frontend page + tests
- [ ] 5. qa: backend service + API + frontend page + tests
- [ ] 6. reviews: backend service + API + frontend page + tests

## Domain Model
### LocalizationProject
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | LocalizationProject display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### TranslationJob
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | TranslationJob display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### TranslationMemory
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | TranslationMemory display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### LocaleVariant
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | LocaleVariant display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### QualityIssue
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | QualityIssue display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ReviewTask
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ReviewTask display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/localization/localization-projects
- POST /api/v1/localization/localization-projects
- GET /api/v1/localization/localization-projects/{id}
- PATCH /api/v1/localization/localization-projects/{id}
- POST /api/v1/localization/localization-projects/{id}/validate
- GET /api/v1/localization/translation-jobs
- POST /api/v1/localization/translation-jobs
- GET /api/v1/localization/translation-jobs/{id}
- PATCH /api/v1/localization/translation-jobs/{id}
- POST /api/v1/localization/translation-jobs/{id}/validate
- GET /api/v1/localization/translation-memories
- POST /api/v1/localization/translation-memories
- GET /api/v1/localization/translation-memories/{id}
- PATCH /api/v1/localization/translation-memories/{id}
- POST /api/v1/localization/translation-memories/{id}/validate
- GET /api/v1/localization/locale-variants
- POST /api/v1/localization/locale-variants
- GET /api/v1/localization/locale-variants/{id}
- PATCH /api/v1/localization/locale-variants/{id}
- POST /api/v1/localization/locale-variants/{id}/validate
- GET /api/v1/localization/quality-issues
- POST /api/v1/localization/quality-issues
- GET /api/v1/localization/quality-issues/{id}
- PATCH /api/v1/localization/quality-issues/{id}
- POST /api/v1/localization/quality-issues/{id}/validate
- POST /api/v1/localization/ai/analyze
- POST /api/v1/localization/workflows/execute
- GET /api/v1/localization/metrics/summary

## Event Contract
- Emits: localizationos.job.completed, localizationos.issue.flagged, localizationos.review.requested
- Consumes: contentos.asset.published, brandvoice.style.updated, videonarrator.transcript.ready
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
- `mvn -pl localizationos/backend clean verify`
- `npm --prefix localizationos/frontend test`
- `npm --prefix localizationos/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `localizationos/.jules/HANDOFF.md`
- Submit PR when done
