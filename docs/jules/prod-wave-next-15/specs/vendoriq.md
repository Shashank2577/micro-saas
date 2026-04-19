# VendorIQ — Next-15 Production Progression Spec

## Product Intent
- App: `vendoriq`
- Domain: Finance Operations
- Outcome: Vendor scorecards, SLA tracking, and renewal risk management
- Primary actors: Vendor manager, Procurement lead, Finance ops

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
- [ ] 1. profiles: backend service + API + frontend page + tests
- [ ] 2. sla: backend service + API + frontend page + tests
- [ ] 3. renewals: backend service + API + frontend page + tests
- [ ] 4. risk: backend service + API + frontend page + tests
- [ ] 5. contracts: backend service + API + frontend page + tests
- [ ] 6. benchmarks: backend service + API + frontend page + tests

## Domain Model
### VendorProfile
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | VendorProfile display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### SLAMetric
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SLAMetric display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### RenewalSchedule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | RenewalSchedule display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### VendorRisk
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | VendorRisk display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ContractTerm
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ContractTerm display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### BenchmarkSnapshot
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | BenchmarkSnapshot display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/vendors/vendor-profiles
- POST /api/v1/vendors/vendor-profiles
- GET /api/v1/vendors/vendor-profiles/{id}
- PATCH /api/v1/vendors/vendor-profiles/{id}
- POST /api/v1/vendors/vendor-profiles/{id}/validate
- GET /api/v1/vendors/slametrics
- POST /api/v1/vendors/slametrics
- GET /api/v1/vendors/slametrics/{id}
- PATCH /api/v1/vendors/slametrics/{id}
- POST /api/v1/vendors/slametrics/{id}/validate
- GET /api/v1/vendors/renewal-schedules
- POST /api/v1/vendors/renewal-schedules
- GET /api/v1/vendors/renewal-schedules/{id}
- PATCH /api/v1/vendors/renewal-schedules/{id}
- POST /api/v1/vendors/renewal-schedules/{id}/validate
- GET /api/v1/vendors/vendor-risks
- POST /api/v1/vendors/vendor-risks
- GET /api/v1/vendors/vendor-risks/{id}
- PATCH /api/v1/vendors/vendor-risks/{id}
- POST /api/v1/vendors/vendor-risks/{id}/validate
- GET /api/v1/vendors/contract-terms
- POST /api/v1/vendors/contract-terms
- GET /api/v1/vendors/contract-terms/{id}
- PATCH /api/v1/vendors/contract-terms/{id}
- POST /api/v1/vendors/contract-terms/{id}/validate
- POST /api/v1/vendors/ai/analyze
- POST /api/v1/vendors/workflows/execute
- GET /api/v1/vendors/metrics/summary

## Event Contract
- Emits: vendoriq.risk.updated, vendoriq.renewal.alerted, vendoriq.sla.breached
- Consumes: invoiceprocessor.invoice.approved, contractportfolio.contract.indexed, procurebot.order.issued
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
- `mvn -pl vendoriq/backend clean verify`
- `npm --prefix vendoriq/frontend test`
- `npm --prefix vendoriq/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `vendoriq/.jules/HANDOFF.md`
- Submit PR when done
