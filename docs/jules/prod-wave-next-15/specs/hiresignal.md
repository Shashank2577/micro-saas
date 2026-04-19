# HireSignal — Next-15 Production Progression Spec

## Product Intent
- App: `hiresignal`
- Domain: HR Talent
- Outcome: Candidate fit scoring and hiring pipeline intelligence
- Primary actors: Recruiter, Hiring manager, People ops

## Current Snapshot
- Score: 60
- Tier: Beta
- Depth: Medium
- Risks: no-frontend-tests, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. candidates: backend service + API + frontend page + tests
- [ ] 2. signals: backend service + API + frontend page + tests
- [ ] 3. stages: backend service + API + frontend page + tests
- [ ] 4. decisions: backend service + API + frontend page + tests
- [ ] 5. pipeline: backend service + API + frontend page + tests
- [ ] 6. requisitions: backend service + API + frontend page + tests

## Domain Model
### CandidateProfile
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | CandidateProfile display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### FitSignal
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | FitSignal display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### InterviewStage
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | InterviewStage display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### HiringDecision
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | HiringDecision display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### PipelineMetric
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PipelineMetric display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### Requisition
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | Requisition display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/hiring/candidate-profiles
- POST /api/v1/hiring/candidate-profiles
- GET /api/v1/hiring/candidate-profiles/{id}
- PATCH /api/v1/hiring/candidate-profiles/{id}
- POST /api/v1/hiring/candidate-profiles/{id}/validate
- GET /api/v1/hiring/fit-signals
- POST /api/v1/hiring/fit-signals
- GET /api/v1/hiring/fit-signals/{id}
- PATCH /api/v1/hiring/fit-signals/{id}
- POST /api/v1/hiring/fit-signals/{id}/validate
- GET /api/v1/hiring/interview-stages
- POST /api/v1/hiring/interview-stages
- GET /api/v1/hiring/interview-stages/{id}
- PATCH /api/v1/hiring/interview-stages/{id}
- POST /api/v1/hiring/interview-stages/{id}/validate
- GET /api/v1/hiring/hiring-decisions
- POST /api/v1/hiring/hiring-decisions
- GET /api/v1/hiring/hiring-decisions/{id}
- PATCH /api/v1/hiring/hiring-decisions/{id}
- POST /api/v1/hiring/hiring-decisions/{id}/validate
- GET /api/v1/hiring/pipeline-metrics
- POST /api/v1/hiring/pipeline-metrics
- GET /api/v1/hiring/pipeline-metrics/{id}
- PATCH /api/v1/hiring/pipeline-metrics/{id}
- POST /api/v1/hiring/pipeline-metrics/{id}/validate
- POST /api/v1/hiring/ai/analyze
- POST /api/v1/hiring/workflows/execute
- GET /api/v1/hiring/metrics/summary

## Event Contract
- Emits: hiresignal.candidate.shortlisted, hiresignal.risk.flagged, hiresignal.hire.confirmed
- Consumes: jobcraftai.posting.published, interviewos.score.submitted, peopleanalytics.org.signal.updated
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
- `mvn -pl hiresignal/backend clean verify`
- `npm --prefix hiresignal/frontend test`
- `npm --prefix hiresignal/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `hiresignal/.jules/HANDOFF.md`
- Submit PR when done
