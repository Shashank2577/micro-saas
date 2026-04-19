# FinanceNarrator — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `financenarrator`
- **Domain:** Finance Intelligence
- **Outcome:** Generate executive-grade financial narratives from structured financial datasets
- **Primary actors:** CFO, FP&A analyst, Board prep owner

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 50
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend unit + integration test suite for service and controller paths.
  - Add frontend component and page interaction tests with Vitest + Testing Library.
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. requests: backend service + controller + frontend page + tests
- [ ] 2. sections: backend service + controller + frontend page + tests
- [ ] 3. metrics: backend service + controller + frontend page + tests
- [ ] 4. tone-profiles: backend service + controller + frontend page + tests
- [ ] 5. reviews: backend service + controller + frontend page + tests
- [ ] 6. exports: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### NarrativeRequest
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | NarrativeRequest display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### NarrativeSection
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | NarrativeSection display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SupportingMetric
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SupportingMetric display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ToneProfile
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ToneProfile display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ApprovalReview
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ApprovalReview display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ExportArtifact
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ExportArtifact display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/narratives/narrative-requests
- POST /api/v1/narratives/narrative-requests
- GET /api/v1/narratives/narrative-requests/{id}
- PATCH /api/v1/narratives/narrative-requests/{id}
- POST /api/v1/narratives/narrative-requests/{id}/validate
- GET /api/v1/narratives/narrative-sections
- POST /api/v1/narratives/narrative-sections
- GET /api/v1/narratives/narrative-sections/{id}
- PATCH /api/v1/narratives/narrative-sections/{id}
- POST /api/v1/narratives/narrative-sections/{id}/validate
- GET /api/v1/narratives/supporting-metrics
- POST /api/v1/narratives/supporting-metrics
- GET /api/v1/narratives/supporting-metrics/{id}
- PATCH /api/v1/narratives/supporting-metrics/{id}
- POST /api/v1/narratives/supporting-metrics/{id}/validate
- GET /api/v1/narratives/tone-profiles
- POST /api/v1/narratives/tone-profiles
- GET /api/v1/narratives/tone-profiles/{id}
- PATCH /api/v1/narratives/tone-profiles/{id}
- POST /api/v1/narratives/tone-profiles/{id}/validate
- GET /api/v1/narratives/approval-reviews
- POST /api/v1/narratives/approval-reviews
- GET /api/v1/narratives/approval-reviews/{id}
- PATCH /api/v1/narratives/approval-reviews/{id}
- POST /api/v1/narratives/approval-reviews/{id}/validate
- POST /api/v1/narratives/ai/analyze
- POST /api/v1/narratives/ai/recommendations
- POST /api/v1/narratives/workflows/execute
- GET /api/v1/narratives/health/contracts
- GET /api/v1/narratives/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- requestsService: create/update/list/getById/delete/validate/simulate
- sectionsService: create/update/list/getById/delete/validate/simulate
- metricsService: create/update/list/getById/delete/validate/simulate
- tone-profilesService: create/update/list/getById/delete/validate/simulate
- reviewsService: create/update/list/getById/delete/validate/simulate
- exportsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** financenarrator.narrative.generated, financenarrator.review.requested, financenarrator.export.completed
- **Consumes:** cashflowanalyzer.insight.published, budgetpilot.variance.alerted, equityintelligence.round.updated
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
- Add backend service + controller tests for critical flows.
- Add frontend component/page tests for key user journeys.
- Add backend Dockerfile to make deploy path repeatable.
- Add app README with setup, contracts, SLOs, and runbooks.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl financenarrator/backend clean verify`
- Frontend:
  - `npm --prefix financenarrator/frontend test`
  - `npm --prefix financenarrator/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
