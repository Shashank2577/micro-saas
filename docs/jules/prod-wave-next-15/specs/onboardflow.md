# OnboardFlow — Next-15 Production Progression Spec

## Product Intent
- App: `onboardflow`
- Domain: HR Operations
- Outcome: Automated 30/60/90 onboarding workflows and completion insights
- Primary actors: People ops, Manager, New employee

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
- [ ] 2. checklists: backend service + API + frontend page + tests
- [ ] 3. tasks: backend service + API + frontend page + tests
- [ ] 4. completion: backend service + API + frontend page + tests
- [ ] 5. escalations: backend service + API + frontend page + tests
- [ ] 6. experience: backend service + API + frontend page + tests

## Domain Model
### OnboardingPlan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | OnboardingPlan display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### MilestoneChecklist
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | MilestoneChecklist display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### TaskAssignment
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | TaskAssignment display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### CompletionSignal
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | CompletionSignal display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### Escalation
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | Escalation display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ExperienceScore
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ExperienceScore display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/onboarding/onboarding-plans
- POST /api/v1/onboarding/onboarding-plans
- GET /api/v1/onboarding/onboarding-plans/{id}
- PATCH /api/v1/onboarding/onboarding-plans/{id}
- POST /api/v1/onboarding/onboarding-plans/{id}/validate
- GET /api/v1/onboarding/milestone-checklists
- POST /api/v1/onboarding/milestone-checklists
- GET /api/v1/onboarding/milestone-checklists/{id}
- PATCH /api/v1/onboarding/milestone-checklists/{id}
- POST /api/v1/onboarding/milestone-checklists/{id}/validate
- GET /api/v1/onboarding/task-assignments
- POST /api/v1/onboarding/task-assignments
- GET /api/v1/onboarding/task-assignments/{id}
- PATCH /api/v1/onboarding/task-assignments/{id}
- POST /api/v1/onboarding/task-assignments/{id}/validate
- GET /api/v1/onboarding/completion-signals
- POST /api/v1/onboarding/completion-signals
- GET /api/v1/onboarding/completion-signals/{id}
- PATCH /api/v1/onboarding/completion-signals/{id}
- POST /api/v1/onboarding/completion-signals/{id}/validate
- GET /api/v1/onboarding/escalations
- POST /api/v1/onboarding/escalations
- GET /api/v1/onboarding/escalations/{id}
- PATCH /api/v1/onboarding/escalations/{id}
- POST /api/v1/onboarding/escalations/{id}/validate
- POST /api/v1/onboarding/ai/analyze
- POST /api/v1/onboarding/workflows/execute
- GET /api/v1/onboarding/metrics/summary

## Event Contract
- Emits: onboardflow.plan.started, onboardflow.milestone.completed, onboardflow.escalation.opened
- Consumes: hiresignal.hire.confirmed, peopleanalytics.org.signal.updated, notificationhub.channel.registered
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
- `mvn -pl onboardflow/backend clean verify`
- `npm --prefix onboardflow/frontend test`
- `npm --prefix onboardflow/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `onboardflow/.jules/HANDOFF.md`
- Submit PR when done
