# AnalyticsBuilder — Next-15 Production Progression Spec

## Product Intent
- App: `analyticsbuilder`
- Domain: Data & Analytics
- Outcome: Self-serve dashboard and report builder with governed metrics
- Primary actors: Analyst, Ops manager, Exec stakeholder

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
- [ ] 1. dashboards: backend service + API + frontend page + tests
- [ ] 2. widgets: backend service + API + frontend page + tests
- [ ] 3. metrics: backend service + API + frontend page + tests
- [ ] 4. queries: backend service + API + frontend page + tests
- [ ] 5. sharing: backend service + API + frontend page + tests
- [ ] 6. reports: backend service + API + frontend page + tests

## Domain Model
### Dashboard
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | Dashboard display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### Widget
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | Widget display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### MetricDefinition
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | MetricDefinition display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### QueryTemplate
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | QueryTemplate display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### SharingPolicy
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SharingPolicy display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ScheduledReport
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ScheduledReport display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/analytics/dashboards
- POST /api/v1/analytics/dashboards
- GET /api/v1/analytics/dashboards/{id}
- PATCH /api/v1/analytics/dashboards/{id}
- POST /api/v1/analytics/dashboards/{id}/validate
- GET /api/v1/analytics/widgets
- POST /api/v1/analytics/widgets
- GET /api/v1/analytics/widgets/{id}
- PATCH /api/v1/analytics/widgets/{id}
- POST /api/v1/analytics/widgets/{id}/validate
- GET /api/v1/analytics/metric-definitions
- POST /api/v1/analytics/metric-definitions
- GET /api/v1/analytics/metric-definitions/{id}
- PATCH /api/v1/analytics/metric-definitions/{id}
- POST /api/v1/analytics/metric-definitions/{id}/validate
- GET /api/v1/analytics/query-templates
- POST /api/v1/analytics/query-templates
- GET /api/v1/analytics/query-templates/{id}
- PATCH /api/v1/analytics/query-templates/{id}
- POST /api/v1/analytics/query-templates/{id}/validate
- GET /api/v1/analytics/sharing-policies
- POST /api/v1/analytics/sharing-policies
- GET /api/v1/analytics/sharing-policies/{id}
- PATCH /api/v1/analytics/sharing-policies/{id}
- POST /api/v1/analytics/sharing-policies/{id}/validate
- POST /api/v1/analytics/ai/analyze
- POST /api/v1/analytics/workflows/execute
- GET /api/v1/analytics/metrics/summary

## Event Contract
- Emits: analyticsbuilder.dashboard.published, analyticsbuilder.report.scheduled, analyticsbuilder.metric.changed
- Consumes: datacatalogai.asset.updated, dataqualityai.rule.failed, usageintelligence.event.received
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
- `mvn -pl analyticsbuilder/backend clean verify`
- `npm --prefix analyticsbuilder/frontend test`
- `npm --prefix analyticsbuilder/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `analyticsbuilder/.jules/HANDOFF.md`
- Submit PR when done
