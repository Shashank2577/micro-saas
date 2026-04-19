# ContractPortfolio — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `contractportfolio`
- **Domain:** Legal Intelligence
- **Outcome:** Portfolio-wide contract extraction, risk indexing, and renewal intelligence
- **Primary actors:** Legal ops, Procurement, Finance controller

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 55
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-frontend, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. contracts: backend service + controller + frontend page + tests
- [ ] 2. clause-extraction: backend service + controller + frontend page + tests
- [ ] 3. obligations: backend service + controller + frontend page + tests
- [ ] 4. risk-scoring: backend service + controller + frontend page + tests
- [ ] 5. renewals: backend service + controller + frontend page + tests
- [ ] 6. counterparties: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### ContractRecord
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ContractRecord display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ClauseExtraction
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ClauseExtraction display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RenewalAlert
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RenewalAlert display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RiskScore
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RiskScore display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ObligationItem
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ObligationItem display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### CounterpartyProfile
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | CounterpartyProfile display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/contracts/contract-records
- POST /api/v1/contracts/contract-records
- GET /api/v1/contracts/contract-records/{id}
- PATCH /api/v1/contracts/contract-records/{id}
- POST /api/v1/contracts/contract-records/{id}/validate
- GET /api/v1/contracts/clause-extractions
- POST /api/v1/contracts/clause-extractions
- GET /api/v1/contracts/clause-extractions/{id}
- PATCH /api/v1/contracts/clause-extractions/{id}
- POST /api/v1/contracts/clause-extractions/{id}/validate
- GET /api/v1/contracts/renewal-alerts
- POST /api/v1/contracts/renewal-alerts
- GET /api/v1/contracts/renewal-alerts/{id}
- PATCH /api/v1/contracts/renewal-alerts/{id}
- POST /api/v1/contracts/renewal-alerts/{id}/validate
- GET /api/v1/contracts/risk-scores
- POST /api/v1/contracts/risk-scores
- GET /api/v1/contracts/risk-scores/{id}
- PATCH /api/v1/contracts/risk-scores/{id}
- POST /api/v1/contracts/risk-scores/{id}/validate
- GET /api/v1/contracts/obligation-items
- POST /api/v1/contracts/obligation-items
- GET /api/v1/contracts/obligation-items/{id}
- PATCH /api/v1/contracts/obligation-items/{id}
- POST /api/v1/contracts/obligation-items/{id}/validate
- POST /api/v1/contracts/ai/analyze
- POST /api/v1/contracts/ai/recommendations
- POST /api/v1/contracts/workflows/execute
- GET /api/v1/contracts/health/contracts
- GET /api/v1/contracts/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- contractsService: create/update/list/getById/delete/validate/simulate
- clause-extractionService: create/update/list/getById/delete/validate/simulate
- obligationsService: create/update/list/getById/delete/validate/simulate
- risk-scoringService: create/update/list/getById/delete/validate/simulate
- renewalsService: create/update/list/getById/delete/validate/simulate
- counterpartiesService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** contractportfolio.contract.indexed, contractportfolio.renewal.alerted, contractportfolio.risk.updated
- **Consumes:** documentvault.document.uploaded, contractsense.contract.analyzed, regulatoryfiling.filing.submitted
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
  - `mvn -pl contractportfolio/backend clean verify`
- Frontend:
  - `npm --prefix contractportfolio/frontend test`
  - `npm --prefix contractportfolio/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
