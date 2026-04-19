# JobCraftAI — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `jobcraftai`
- **Domain:** HR Talent
- **Outcome:** Generate inclusive, high-converting job descriptions with hiring analytics
- **Primary actors:** Recruiter, Hiring manager, People ops

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 35
- **Tier:** Incubating
- **Depth:** Shallow
- **Known structural risks:** no-db-migrations, no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend unit + integration test suite for service and controller paths.
  - Add frontend component and page interaction tests with Vitest + Testing Library.
  - Introduce Flyway V1 baseline and follow-up incremental migrations.
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. role-profiles: backend service + controller + frontend page + tests
- [ ] 2. drafting: backend service + controller + frontend page + tests
- [ ] 3. bias-check: backend service + controller + frontend page + tests
- [ ] 4. publishing: backend service + controller + frontend page + tests
- [ ] 5. performance: backend service + controller + frontend page + tests
- [ ] 6. iterations: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### JobPosting
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | JobPosting display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RoleProfile
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RoleProfile display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### BiasCheck
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | BiasCheck display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ChannelPerformance
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ChannelPerformance display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### CandidatePersona
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | CandidatePersona display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RevisionLog
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RevisionLog display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/job-postings/job-postings
- POST /api/v1/job-postings/job-postings
- GET /api/v1/job-postings/job-postings/{id}
- PATCH /api/v1/job-postings/job-postings/{id}
- POST /api/v1/job-postings/job-postings/{id}/validate
- GET /api/v1/job-postings/role-profiles
- POST /api/v1/job-postings/role-profiles
- GET /api/v1/job-postings/role-profiles/{id}
- PATCH /api/v1/job-postings/role-profiles/{id}
- POST /api/v1/job-postings/role-profiles/{id}/validate
- GET /api/v1/job-postings/bias-checks
- POST /api/v1/job-postings/bias-checks
- GET /api/v1/job-postings/bias-checks/{id}
- PATCH /api/v1/job-postings/bias-checks/{id}
- POST /api/v1/job-postings/bias-checks/{id}/validate
- GET /api/v1/job-postings/channel-performances
- POST /api/v1/job-postings/channel-performances
- GET /api/v1/job-postings/channel-performances/{id}
- PATCH /api/v1/job-postings/channel-performances/{id}
- POST /api/v1/job-postings/channel-performances/{id}/validate
- GET /api/v1/job-postings/candidate-personas
- POST /api/v1/job-postings/candidate-personas
- GET /api/v1/job-postings/candidate-personas/{id}
- PATCH /api/v1/job-postings/candidate-personas/{id}
- POST /api/v1/job-postings/candidate-personas/{id}/validate
- POST /api/v1/job-postings/ai/analyze
- POST /api/v1/job-postings/ai/recommendations
- POST /api/v1/job-postings/workflows/execute
- GET /api/v1/job-postings/health/contracts
- GET /api/v1/job-postings/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- role-profilesService: create/update/list/getById/delete/validate/simulate
- draftingService: create/update/list/getById/delete/validate/simulate
- bias-checkService: create/update/list/getById/delete/validate/simulate
- publishingService: create/update/list/getById/delete/validate/simulate
- performanceService: create/update/list/getById/delete/validate/simulate
- iterationsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** jobcraftai.posting.published, jobcraftai.bias.issue.flagged, jobcraftai.channel.performance.updated
- **Consumes:** hiresignal.requisition.created, peopleanalytics.role.benchmark.updated, compbenchmark.band.changed
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
- Add Flyway baseline and incremental migrations for all persisted entities.
- Add backend service + controller tests for critical flows.
- Add frontend component/page tests for key user journeys.
- Add backend Dockerfile to make deploy path repeatable.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl jobcraftai/backend clean verify`
- Frontend:
  - `npm --prefix jobcraftai/frontend test`
  - `npm --prefix jobcraftai/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
