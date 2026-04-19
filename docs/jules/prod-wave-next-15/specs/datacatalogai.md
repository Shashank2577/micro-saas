# DataCatalogAI — Next-15 Production Progression Spec

## Product Intent
- App: `datacatalogai`
- Domain: Data Governance
- Outcome: Data asset catalog with ownership, lineage context, and semantic enrichment
- Primary actors: Data steward, Data engineer, Analyst

## Current Snapshot
- Score: 60
- Tier: Beta
- Depth: Deep
- Risks: no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add backend service/controller tests for critical paths.
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. assets: backend service + API + frontend page + tests
- [ ] 2. ownership: backend service + API + frontend page + tests
- [ ] 3. lineage: backend service + API + frontend page + tests
- [ ] 4. tags: backend service + API + frontend page + tests
- [ ] 5. policies: backend service + API + frontend page + tests
- [ ] 6. discovery: backend service + API + frontend page + tests

## Domain Model
### DataAsset
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | DataAsset display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### OwnershipRecord
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | OwnershipRecord display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### LineageRef
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | LineageRef display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### SemanticTag
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SemanticTag display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### PolicyBinding
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PolicyBinding display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### DiscoveryQuery
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | DiscoveryQuery display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/data-catalog/data-assets
- POST /api/v1/data-catalog/data-assets
- GET /api/v1/data-catalog/data-assets/{id}
- PATCH /api/v1/data-catalog/data-assets/{id}
- POST /api/v1/data-catalog/data-assets/{id}/validate
- GET /api/v1/data-catalog/ownership-records
- POST /api/v1/data-catalog/ownership-records
- GET /api/v1/data-catalog/ownership-records/{id}
- PATCH /api/v1/data-catalog/ownership-records/{id}
- POST /api/v1/data-catalog/ownership-records/{id}/validate
- GET /api/v1/data-catalog/lineage-refs
- POST /api/v1/data-catalog/lineage-refs
- GET /api/v1/data-catalog/lineage-refs/{id}
- PATCH /api/v1/data-catalog/lineage-refs/{id}
- POST /api/v1/data-catalog/lineage-refs/{id}/validate
- GET /api/v1/data-catalog/semantic-tags
- POST /api/v1/data-catalog/semantic-tags
- GET /api/v1/data-catalog/semantic-tags/{id}
- PATCH /api/v1/data-catalog/semantic-tags/{id}
- POST /api/v1/data-catalog/semantic-tags/{id}/validate
- GET /api/v1/data-catalog/policy-bindings
- POST /api/v1/data-catalog/policy-bindings
- GET /api/v1/data-catalog/policy-bindings/{id}
- PATCH /api/v1/data-catalog/policy-bindings/{id}
- POST /api/v1/data-catalog/policy-bindings/{id}/validate
- POST /api/v1/data-catalog/ai/analyze
- POST /api/v1/data-catalog/workflows/execute
- GET /api/v1/data-catalog/metrics/summary

## Event Contract
- Emits: datacatalogai.asset.registered, datacatalogai.policy.bound, datacatalogai.asset.deprecated
- Consumes: datalineagetracker.link.updated, dataqualityai.drift.detected, datagovernanceos.policy.updated
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
- `mvn -pl datacatalogai/backend clean verify`
- `npm --prefix datacatalogai/frontend test`
- `npm --prefix datacatalogai/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `datacatalogai/.jules/HANDOFF.md`
- Submit PR when done
