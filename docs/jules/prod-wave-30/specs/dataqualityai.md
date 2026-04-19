# DataQualityAI — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `dataqualityai`
- **Domain:** Data Reliability
- **Outcome:** Data quality rule authoring, drift detection, and automated remediation recommendations
- **Primary actors:** Data engineer, Analytics engineer, Platform owner

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 50
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-db-migrations, no-backend-tests, no-frontend-tests, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend unit + integration test suite for service and controller paths.
  - Add frontend component and page interaction tests with Vitest + Testing Library.
  - Introduce Flyway V1 baseline and follow-up incremental migrations.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. rules: backend service + controller + frontend page + tests
- [ ] 2. profiling: backend service + controller + frontend page + tests
- [ ] 3. validation: backend service + controller + frontend page + tests
- [ ] 4. drift: backend service + controller + frontend page + tests
- [ ] 5. issues: backend service + controller + frontend page + tests
- [ ] 6. remediation: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### QualityRule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | QualityRule display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### DatasetProfile
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | DatasetProfile display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### DriftEvent
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | DriftEvent display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ValidationRun
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ValidationRun display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### IssueTicket
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | IssueTicket display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RemediationSuggestion
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RemediationSuggestion display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/data-quality/quality-rules
- POST /api/v1/data-quality/quality-rules
- GET /api/v1/data-quality/quality-rules/{id}
- PATCH /api/v1/data-quality/quality-rules/{id}
- POST /api/v1/data-quality/quality-rules/{id}/validate
- GET /api/v1/data-quality/dataset-profiles
- POST /api/v1/data-quality/dataset-profiles
- GET /api/v1/data-quality/dataset-profiles/{id}
- PATCH /api/v1/data-quality/dataset-profiles/{id}
- POST /api/v1/data-quality/dataset-profiles/{id}/validate
- GET /api/v1/data-quality/drift-events
- POST /api/v1/data-quality/drift-events
- GET /api/v1/data-quality/drift-events/{id}
- PATCH /api/v1/data-quality/drift-events/{id}
- POST /api/v1/data-quality/drift-events/{id}/validate
- GET /api/v1/data-quality/validation-runs
- POST /api/v1/data-quality/validation-runs
- GET /api/v1/data-quality/validation-runs/{id}
- PATCH /api/v1/data-quality/validation-runs/{id}
- POST /api/v1/data-quality/validation-runs/{id}/validate
- GET /api/v1/data-quality/issue-tickets
- POST /api/v1/data-quality/issue-tickets
- GET /api/v1/data-quality/issue-tickets/{id}
- PATCH /api/v1/data-quality/issue-tickets/{id}
- POST /api/v1/data-quality/issue-tickets/{id}/validate
- POST /api/v1/data-quality/ai/analyze
- POST /api/v1/data-quality/ai/recommendations
- POST /api/v1/data-quality/workflows/execute
- GET /api/v1/data-quality/health/contracts
- GET /api/v1/data-quality/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- rulesService: create/update/list/getById/delete/validate/simulate
- profilingService: create/update/list/getById/delete/validate/simulate
- validationService: create/update/list/getById/delete/validate/simulate
- driftService: create/update/list/getById/delete/validate/simulate
- issuesService: create/update/list/getById/delete/validate/simulate
- remediationService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** dataqualityai.rule.failed, dataqualityai.drift.detected, dataqualityai.remediation.suggested
- **Consumes:** datacatalogai.asset.registered, datalineagetracker.link.updated, pipelineorchestrator.run.completed
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
- Align consumes events with ecosystem producer event names.
- Add Flyway baseline and incremental migrations for all persisted entities.
- Add backend service + controller tests for critical flows.
- Add frontend component/page tests for key user journeys.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl dataqualityai/backend clean verify`
- Frontend:
  - `npm --prefix dataqualityai/frontend test`
  - `npm --prefix dataqualityai/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
