# ConstructionIQ — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `constructioniq`
- **Domain:** Construction Operations
- **Outcome:** Construction project risk, budget, timeline, and resource intelligence
- **Primary actors:** Project manager, Site supervisor, Operations director

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 45
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend unit + integration test suite for service and controller paths.
  - Add frontend component and page interaction tests with Vitest + Testing Library.
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. projects: backend service + controller + frontend page + tests
- [ ] 2. site-reports: backend service + controller + frontend page + tests
- [ ] 3. milestones: backend service + controller + frontend page + tests
- [ ] 4. safety: backend service + controller + frontend page + tests
- [ ] 5. change-orders: backend service + controller + frontend page + tests
- [ ] 6. resources: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### ConstructionProject
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ConstructionProject display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SiteReport
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SiteReport display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### Milestone
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | Milestone display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SafetyIncident
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SafetyIncident display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ChangeOrder
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ChangeOrder display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ResourceAllocation
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ResourceAllocation display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/projects/construction-projects
- POST /api/v1/projects/construction-projects
- GET /api/v1/projects/construction-projects/{id}
- PATCH /api/v1/projects/construction-projects/{id}
- POST /api/v1/projects/construction-projects/{id}/validate
- GET /api/v1/projects/site-reports
- POST /api/v1/projects/site-reports
- GET /api/v1/projects/site-reports/{id}
- PATCH /api/v1/projects/site-reports/{id}
- POST /api/v1/projects/site-reports/{id}/validate
- GET /api/v1/projects/milestones
- POST /api/v1/projects/milestones
- GET /api/v1/projects/milestones/{id}
- PATCH /api/v1/projects/milestones/{id}
- POST /api/v1/projects/milestones/{id}/validate
- GET /api/v1/projects/safety-incidents
- POST /api/v1/projects/safety-incidents
- GET /api/v1/projects/safety-incidents/{id}
- PATCH /api/v1/projects/safety-incidents/{id}
- POST /api/v1/projects/safety-incidents/{id}/validate
- GET /api/v1/projects/change-orders
- POST /api/v1/projects/change-orders
- GET /api/v1/projects/change-orders/{id}
- PATCH /api/v1/projects/change-orders/{id}
- POST /api/v1/projects/change-orders/{id}/validate
- POST /api/v1/projects/ai/analyze
- POST /api/v1/projects/ai/recommendations
- POST /api/v1/projects/workflows/execute
- GET /api/v1/projects/health/contracts
- GET /api/v1/projects/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- projectsService: create/update/list/getById/delete/validate/simulate
- site-reportsService: create/update/list/getById/delete/validate/simulate
- milestonesService: create/update/list/getById/delete/validate/simulate
- safetyService: create/update/list/getById/delete/validate/simulate
- change-ordersService: create/update/list/getById/delete/validate/simulate
- resourcesService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** constructioniq.project.risk.updated, constructioniq.milestone.delayed, constructioniq.safety.incident.created
- **Consumes:** budgetpilot.variance.alerted, notificationhub.channel.registered, documentvault.document.uploaded
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
  - `mvn -pl constructioniq/backend clean verify`
- Frontend:
  - `npm --prefix constructioniq/frontend test`
  - `npm --prefix constructioniq/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
