# LegalResearch — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `legalresearch`
- **Domain:** Legal Intelligence
- **Outcome:** Legal research workflow with citation tracking, precedent ranking, and memo generation
- **Primary actors:** Legal analyst, Counsel, Compliance manager

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 50
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-frontend, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. queries: backend service + controller + frontend page + tests
- [ ] 2. sources: backend service + controller + frontend page + tests
- [ ] 3. precedents: backend service + controller + frontend page + tests
- [ ] 4. memos: backend service + controller + frontend page + tests
- [ ] 5. arguments: backend service + controller + frontend page + tests
- [ ] 6. reviews: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### ResearchQuery
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ResearchQuery display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SourceCitation
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SourceCitation display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### PrecedentNote
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | PrecedentNote display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### MemoDraft
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | MemoDraft display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ArgumentGraph
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ArgumentGraph display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ReviewComment
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ReviewComment display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/research/research-querys
- POST /api/v1/research/research-querys
- GET /api/v1/research/research-querys/{id}
- PATCH /api/v1/research/research-querys/{id}
- POST /api/v1/research/research-querys/{id}/validate
- GET /api/v1/research/source-citations
- POST /api/v1/research/source-citations
- GET /api/v1/research/source-citations/{id}
- PATCH /api/v1/research/source-citations/{id}
- POST /api/v1/research/source-citations/{id}/validate
- GET /api/v1/research/precedent-notes
- POST /api/v1/research/precedent-notes
- GET /api/v1/research/precedent-notes/{id}
- PATCH /api/v1/research/precedent-notes/{id}
- POST /api/v1/research/precedent-notes/{id}/validate
- GET /api/v1/research/memo-drafts
- POST /api/v1/research/memo-drafts
- GET /api/v1/research/memo-drafts/{id}
- PATCH /api/v1/research/memo-drafts/{id}
- POST /api/v1/research/memo-drafts/{id}/validate
- GET /api/v1/research/argument-graphs
- POST /api/v1/research/argument-graphs
- GET /api/v1/research/argument-graphs/{id}
- PATCH /api/v1/research/argument-graphs/{id}
- POST /api/v1/research/argument-graphs/{id}/validate
- POST /api/v1/research/ai/analyze
- POST /api/v1/research/ai/recommendations
- POST /api/v1/research/workflows/execute
- GET /api/v1/research/health/contracts
- GET /api/v1/research/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- queriesService: create/update/list/getById/delete/validate/simulate
- sourcesService: create/update/list/getById/delete/validate/simulate
- precedentsService: create/update/list/getById/delete/validate/simulate
- memosService: create/update/list/getById/delete/validate/simulate
- argumentsService: create/update/list/getById/delete/validate/simulate
- reviewsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** legalresearch.memo.generated, legalresearch.citation.added, legalresearch.review.requested
- **Consumes:** complianceradar.change.detected, policyforge.policy.published, contractsense.risk.flagged
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
  - `mvn -pl legalresearch/backend clean verify`
- Frontend:
  - `npm --prefix legalresearch/frontend test`
  - `npm --prefix legalresearch/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
