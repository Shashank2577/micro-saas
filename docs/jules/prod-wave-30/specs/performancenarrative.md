# PerformanceNarrative — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `performancenarrative`
- **Domain:** HR Talent
- **Outcome:** Performance review narrative drafting with calibration support
- **Primary actors:** Manager, HRBP, People ops

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 50
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-frontend, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. cycles: backend service + controller + frontend page + tests
- [ ] 2. reviews: backend service + controller + frontend page + tests
- [ ] 3. evidence: backend service + controller + frontend page + tests
- [ ] 4. narratives: backend service + controller + frontend page + tests
- [ ] 5. calibration: backend service + controller + frontend page + tests
- [ ] 6. feedback: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### ReviewCycle
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ReviewCycle display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### EmployeeReview
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | EmployeeReview display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### CalibrationNote
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | CalibrationNote display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### GoalEvidence
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | GoalEvidence display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### NarrativeDraft
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | NarrativeDraft display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### FeedbackItem
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | FeedbackItem display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/performance/review-cycles
- POST /api/v1/performance/review-cycles
- GET /api/v1/performance/review-cycles/{id}
- PATCH /api/v1/performance/review-cycles/{id}
- POST /api/v1/performance/review-cycles/{id}/validate
- GET /api/v1/performance/employee-reviews
- POST /api/v1/performance/employee-reviews
- GET /api/v1/performance/employee-reviews/{id}
- PATCH /api/v1/performance/employee-reviews/{id}
- POST /api/v1/performance/employee-reviews/{id}/validate
- GET /api/v1/performance/calibration-notes
- POST /api/v1/performance/calibration-notes
- GET /api/v1/performance/calibration-notes/{id}
- PATCH /api/v1/performance/calibration-notes/{id}
- POST /api/v1/performance/calibration-notes/{id}/validate
- GET /api/v1/performance/goal-evidences
- POST /api/v1/performance/goal-evidences
- GET /api/v1/performance/goal-evidences/{id}
- PATCH /api/v1/performance/goal-evidences/{id}
- POST /api/v1/performance/goal-evidences/{id}/validate
- GET /api/v1/performance/narrative-drafts
- POST /api/v1/performance/narrative-drafts
- GET /api/v1/performance/narrative-drafts/{id}
- PATCH /api/v1/performance/narrative-drafts/{id}
- POST /api/v1/performance/narrative-drafts/{id}/validate
- POST /api/v1/performance/ai/analyze
- POST /api/v1/performance/ai/recommendations
- POST /api/v1/performance/workflows/execute
- GET /api/v1/performance/health/contracts
- GET /api/v1/performance/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- cyclesService: create/update/list/getById/delete/validate/simulate
- reviewsService: create/update/list/getById/delete/validate/simulate
- evidenceService: create/update/list/getById/delete/validate/simulate
- narrativesService: create/update/list/getById/delete/validate/simulate
- calibrationService: create/update/list/getById/delete/validate/simulate
- feedbackService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** performancenarrative.review.drafted, performancenarrative.calibration.flagged, performancenarrative.review.finalized
- **Consumes:** goaltracker.goal.updated, peopleanalytics.signal.updated, retentionsignal.risk.detected
- Every emitted event must include: `event_id`, `tenant_id`, `occurred_at`, `source_app`, `payload`.
- Consumed events must be idempotent and deduplicated by `event_id`.
- Register/refresh contracts in `integration-manifest.json` and ensure Nexus Hub compatibility.

## 8) Frontend Requirements (Next.js App Router)
- Build operator-facing pages for all modules in section 3.
- Include:
  - Table/list view with filters and pagination
  - Detail page with timeline/activity
  - Create/edit forms with schema validation
  - Error/empty/loading states
  - Toast notifications and optimistic updates where safe
- Add component and page tests for critical paths.

## 9) AI/LLM Requirements
- Use LiteLLM through a dedicated client wrapper.
- Include:
  - Request schema validation
  - Timeout, retry (exponential backoff), and fallback behavior
  - Prompt templates versioned in code
  - Structured JSON output parsing with guardrails
  - Audit log of AI decisions (traceability)

## 10) Non-Functional & Security Requirements
- Multi-tenant hard isolation by `tenant_id` on every query and mutation.
- RBAC on every endpoint (minimum roles: admin/operator/viewer).
- PII fields encrypted at rest where applicable.
- Metrics:
  - P95 API latency < 300ms for reads, < 600ms for writes
  - Background jobs visible in metrics/health endpoints
- Observability:
  - Structured logs with correlation id
  - Error budget and alert thresholds
- Resilience:
  - Idempotency keys for external side-effects
  - Transaction boundaries for multi-write operations

## 11) Detailed Acceptance Criteria (must all pass)
1. All 6 module slices are implemented and wired from UI to backend.
2. All endpoints return tenant-scoped data and reject cross-tenant access.
3. All write endpoints publish documented domain events with payload schema validation.
4. OpenAPI docs render and include every endpoint in this spec.
5. Backend tests cover happy path, validation failures, and permission failures.
6. Frontend tests cover critical user flows and error states.
7. Async workflows are queued via pgmq and are idempotent on retries.
8. LiteLLM integration is guarded with timeout/retry/circuit breaker policy.
9. All env var contracts are documented and validated on startup.
10. No TODO/FIXME placeholders in production code paths.
11. Integration manifest contracts match implemented emits/consumes events.
12. PR includes .jules/DETAILED_SPEC.md, IMPLEMENTATION_LOG.md, and HANDOFF.md.

## 12) Integration Opportunities (what this app should connect to)
- Add backend Dockerfile to make deploy path repeatable.
- Add frontend app shell for operator workflows and observability.
- Add app README with setup, contracts, SLOs, and runbooks.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl performancenarrative/backend clean verify`
- Frontend:
  - `npm --prefix performancenarrative/frontend test`
  - `npm --prefix performancenarrative/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
