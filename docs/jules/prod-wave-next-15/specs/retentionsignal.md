# RetentionSignal — Next-15 Production Progression Spec

## Product Intent
- App: `retentionsignal`
- Domain: HR Talent
- Outcome: Employee flight-risk modeling and intervention recommendation engine
- Primary actors: HRBP, Manager, People ops

## Current Snapshot
- Score: 60
- Tier: Beta
- Depth: Shallow
- Risks: no-frontend-tests, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. risk-models: backend service + API + frontend page + tests
- [ ] 2. interventions: backend service + API + frontend page + tests
- [ ] 3. signals: backend service + API + frontend page + tests
- [ ] 4. drivers: backend service + API + frontend page + tests
- [ ] 5. followups: backend service + API + frontend page + tests
- [ ] 6. outcomes: backend service + API + frontend page + tests

## Domain Model
### RetentionRisk
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | RetentionRisk display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### InterventionPlan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | InterventionPlan display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### SentimentSignal
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SentimentSignal display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### RiskDriver
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | RiskDriver display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### FollowupTask
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | FollowupTask display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### OutcomeRecord
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | OutcomeRecord display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/retention/retention-risks
- POST /api/v1/retention/retention-risks
- GET /api/v1/retention/retention-risks/{id}
- PATCH /api/v1/retention/retention-risks/{id}
- POST /api/v1/retention/retention-risks/{id}/validate
- GET /api/v1/retention/intervention-plans
- POST /api/v1/retention/intervention-plans
- GET /api/v1/retention/intervention-plans/{id}
- PATCH /api/v1/retention/intervention-plans/{id}
- POST /api/v1/retention/intervention-plans/{id}/validate
- GET /api/v1/retention/sentiment-signals
- POST /api/v1/retention/sentiment-signals
- GET /api/v1/retention/sentiment-signals/{id}
- PATCH /api/v1/retention/sentiment-signals/{id}
- POST /api/v1/retention/sentiment-signals/{id}/validate
- GET /api/v1/retention/risk-drivers
- POST /api/v1/retention/risk-drivers
- GET /api/v1/retention/risk-drivers/{id}
- PATCH /api/v1/retention/risk-drivers/{id}
- POST /api/v1/retention/risk-drivers/{id}/validate
- GET /api/v1/retention/followup-tasks
- POST /api/v1/retention/followup-tasks
- GET /api/v1/retention/followup-tasks/{id}
- PATCH /api/v1/retention/followup-tasks/{id}
- POST /api/v1/retention/followup-tasks/{id}/validate
- POST /api/v1/retention/ai/analyze
- POST /api/v1/retention/workflows/execute
- GET /api/v1/retention/metrics/summary

## Event Contract
- Emits: retentionsignal.risk.detected, retentionsignal.intervention.recommended, retentionsignal.outcome.recorded
- Consumes: peopleanalytics.org.signal.updated, performancenarrative.review.finalized, engagementpulse.signal.updated
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
- `mvn -pl retentionsignal/backend clean verify`
- `npm --prefix retentionsignal/frontend test`
- `npm --prefix retentionsignal/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `retentionsignal/.jules/HANDOFF.md`
- Submit PR when done
