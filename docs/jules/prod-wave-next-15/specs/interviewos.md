# InterviewOS — Next-15 Production Progression Spec

## Product Intent
- App: `interviewos`
- Domain: HR Talent
- Outcome: Structured interview orchestration and evaluator consistency scoring
- Primary actors: Recruiter, Interviewer, Hiring manager

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
- [ ] 1. plans: backend service + API + frontend page + tests
- [ ] 2. questions: backend service + API + frontend page + tests
- [ ] 3. scorecards: backend service + API + frontend page + tests
- [ ] 4. evaluations: backend service + API + frontend page + tests
- [ ] 5. calibration: backend service + API + frontend page + tests
- [ ] 6. decisions: backend service + API + frontend page + tests

## Domain Model
### InterviewPlan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | InterviewPlan display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### QuestionBank
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | QuestionBank display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### Scorecard
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | Scorecard display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### EvaluationRecord
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | EvaluationRecord display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### CalibrationSignal
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | CalibrationSignal display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### DecisionPacket
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | DecisionPacket display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/interviews/interview-plans
- POST /api/v1/interviews/interview-plans
- GET /api/v1/interviews/interview-plans/{id}
- PATCH /api/v1/interviews/interview-plans/{id}
- POST /api/v1/interviews/interview-plans/{id}/validate
- GET /api/v1/interviews/question-banks
- POST /api/v1/interviews/question-banks
- GET /api/v1/interviews/question-banks/{id}
- PATCH /api/v1/interviews/question-banks/{id}
- POST /api/v1/interviews/question-banks/{id}/validate
- GET /api/v1/interviews/scorecards
- POST /api/v1/interviews/scorecards
- GET /api/v1/interviews/scorecards/{id}
- PATCH /api/v1/interviews/scorecards/{id}
- POST /api/v1/interviews/scorecards/{id}/validate
- GET /api/v1/interviews/evaluation-records
- POST /api/v1/interviews/evaluation-records
- GET /api/v1/interviews/evaluation-records/{id}
- PATCH /api/v1/interviews/evaluation-records/{id}
- POST /api/v1/interviews/evaluation-records/{id}/validate
- GET /api/v1/interviews/calibration-signals
- POST /api/v1/interviews/calibration-signals
- GET /api/v1/interviews/calibration-signals/{id}
- PATCH /api/v1/interviews/calibration-signals/{id}
- POST /api/v1/interviews/calibration-signals/{id}/validate
- POST /api/v1/interviews/ai/analyze
- POST /api/v1/interviews/workflows/execute
- GET /api/v1/interviews/metrics/summary

## Event Contract
- Emits: interviewos.score.submitted, interviewos.calibration.required, interviewos.packet.ready
- Consumes: hiresignal.candidate.shortlisted, jobcraftai.posting.published, performancenarrative.review.finalized
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
- `mvn -pl interviewos/backend clean verify`
- `npm --prefix interviewos/frontend test`
- `npm --prefix interviewos/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `interviewos/.jules/HANDOFF.md`
- Submit PR when done
