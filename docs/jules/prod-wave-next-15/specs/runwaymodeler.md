# RunwayModeler — Next-15 Production Progression Spec

## Product Intent
- App: `runwaymodeler`
- Domain: Finance Strategy
- Outcome: Cash runway and scenario modeling for strategic planning
- Primary actors: Founder, CFO, FP&A analyst

## Current Snapshot
- Score: 58
- Tier: Alpha
- Depth: Shallow
- Risks: no-frontend, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. scenarios: backend service + API + frontend page + tests
- [ ] 2. projections: backend service + API + frontend page + tests
- [ ] 3. headcount: backend service + API + frontend page + tests
- [ ] 4. levers: backend service + API + frontend page + tests
- [ ] 5. assumptions: backend service + API + frontend page + tests
- [ ] 6. sensitivity: backend service + API + frontend page + tests

## Domain Model
### RunwayScenario
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | RunwayScenario display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### CashProjection
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | CashProjection display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### HeadcountPlan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | HeadcountPlan display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### SpendLeverage
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SpendLeverage display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### FundingAssumption
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | FundingAssumption display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### SensitivityModel
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SensitivityModel display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/runway/runway-scenarios
- POST /api/v1/runway/runway-scenarios
- GET /api/v1/runway/runway-scenarios/{id}
- PATCH /api/v1/runway/runway-scenarios/{id}
- POST /api/v1/runway/runway-scenarios/{id}/validate
- GET /api/v1/runway/cash-projections
- POST /api/v1/runway/cash-projections
- GET /api/v1/runway/cash-projections/{id}
- PATCH /api/v1/runway/cash-projections/{id}
- POST /api/v1/runway/cash-projections/{id}/validate
- GET /api/v1/runway/headcount-plans
- POST /api/v1/runway/headcount-plans
- GET /api/v1/runway/headcount-plans/{id}
- PATCH /api/v1/runway/headcount-plans/{id}
- POST /api/v1/runway/headcount-plans/{id}/validate
- GET /api/v1/runway/spend-leverages
- POST /api/v1/runway/spend-leverages
- GET /api/v1/runway/spend-leverages/{id}
- PATCH /api/v1/runway/spend-leverages/{id}
- POST /api/v1/runway/spend-leverages/{id}/validate
- GET /api/v1/runway/funding-assumptions
- POST /api/v1/runway/funding-assumptions
- GET /api/v1/runway/funding-assumptions/{id}
- PATCH /api/v1/runway/funding-assumptions/{id}
- POST /api/v1/runway/funding-assumptions/{id}/validate
- POST /api/v1/runway/ai/analyze
- POST /api/v1/runway/workflows/execute
- GET /api/v1/runway/metrics/summary

## Event Contract
- Emits: runwaymodeler.scenario.created, runwaymodeler.runway.alerted, runwaymodeler.plan.recommended
- Consumes: cashflowai.position.updated, budgetpilot.reforecast.completed, equityintelligence.round.updated
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
- `mvn -pl runwaymodeler/backend clean verify`
- `npm --prefix runwaymodeler/frontend test`
- `npm --prefix runwaymodeler/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `runwaymodeler/.jules/HANDOFF.md`
- Submit PR when done
