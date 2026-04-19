# PeopleAnalytics — Next-15 Production Progression Spec

## Product Intent
- App: `peopleanalytics`
- Domain: HR Analytics
- Outcome: Workforce health, productivity, and org planning analytics
- Primary actors: People analyst, HRBP, Leadership

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
- [ ] 1. org-snapshots: backend service + API + frontend page + tests
- [ ] 2. headcount: backend service + API + frontend page + tests
- [ ] 3. attrition: backend service + API + frontend page + tests
- [ ] 4. engagement: backend service + API + frontend page + tests
- [ ] 5. performance: backend service + API + frontend page + tests
- [ ] 6. planning: backend service + API + frontend page + tests

## Domain Model
### OrgSnapshot
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | OrgSnapshot display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### HeadcountMetric
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | HeadcountMetric display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### AttritionSignal
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | AttritionSignal display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### EngagementIndicator
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | EngagementIndicator display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### PerformanceTrend
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PerformanceTrend display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### PlanningScenario
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PlanningScenario display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/people-analytics/org-snapshots
- POST /api/v1/people-analytics/org-snapshots
- GET /api/v1/people-analytics/org-snapshots/{id}
- PATCH /api/v1/people-analytics/org-snapshots/{id}
- POST /api/v1/people-analytics/org-snapshots/{id}/validate
- GET /api/v1/people-analytics/headcount-metrics
- POST /api/v1/people-analytics/headcount-metrics
- GET /api/v1/people-analytics/headcount-metrics/{id}
- PATCH /api/v1/people-analytics/headcount-metrics/{id}
- POST /api/v1/people-analytics/headcount-metrics/{id}/validate
- GET /api/v1/people-analytics/attrition-signals
- POST /api/v1/people-analytics/attrition-signals
- GET /api/v1/people-analytics/attrition-signals/{id}
- PATCH /api/v1/people-analytics/attrition-signals/{id}
- POST /api/v1/people-analytics/attrition-signals/{id}/validate
- GET /api/v1/people-analytics/engagement-indicators
- POST /api/v1/people-analytics/engagement-indicators
- GET /api/v1/people-analytics/engagement-indicators/{id}
- PATCH /api/v1/people-analytics/engagement-indicators/{id}
- POST /api/v1/people-analytics/engagement-indicators/{id}/validate
- GET /api/v1/people-analytics/performance-trends
- POST /api/v1/people-analytics/performance-trends
- GET /api/v1/people-analytics/performance-trends/{id}
- PATCH /api/v1/people-analytics/performance-trends/{id}
- POST /api/v1/people-analytics/performance-trends/{id}/validate
- POST /api/v1/people-analytics/ai/analyze
- POST /api/v1/people-analytics/workflows/execute
- GET /api/v1/people-analytics/metrics/summary

## Event Contract
- Emits: peopleanalytics.org.signal.updated, peopleanalytics.attrition.risk.detected, peopleanalytics.scenario.generated
- Consumes: performancenarrative.review.finalized, retentionsignal.risk.detected, onboardflow.milestone.completed
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
- `mvn -pl peopleanalytics/backend clean verify`
- `npm --prefix peopleanalytics/frontend test`
- `npm --prefix peopleanalytics/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `peopleanalytics/.jules/HANDOFF.md`
- Submit PR when done
