# CashflowAI — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `cashflowai`
- **Domain:** Finance Operations
- **Outcome:** Near-term cash position forecasting and shortfall mitigation automation
- **Primary actors:** Treasury analyst, CFO, Finance manager

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 55
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend unit + integration test suite for service and controller paths.
  - Add frontend component and page interaction tests with Vitest + Testing Library.
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. positions: backend service + controller + frontend page + tests
- [ ] 2. forecasts: backend service + controller + frontend page + tests
- [ ] 3. shortfalls: backend service + controller + frontend page + tests
- [ ] 4. mitigations: backend service + controller + frontend page + tests
- [ ] 5. scenarios: backend service + controller + frontend page + tests
- [ ] 6. events: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### CashPosition
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | CashPosition display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |
| as_of | date | Snapshot date |
| available_cash | numeric(14,2) | Available cash |
| restricted_cash | numeric(14,2) | Restricted cash |

### LiquidityForecast
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | LiquidityForecast display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ShortfallAlert
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ShortfallAlert display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### MitigationOption
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | MitigationOption display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ScenarioRun
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ScenarioRun display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### FundingEvent
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | FundingEvent display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/cash-position/cash-positions
- POST /api/v1/cash-position/cash-positions
- GET /api/v1/cash-position/cash-positions/{id}
- PATCH /api/v1/cash-position/cash-positions/{id}
- POST /api/v1/cash-position/cash-positions/{id}/validate
- GET /api/v1/cash-position/liquidity-forecasts
- POST /api/v1/cash-position/liquidity-forecasts
- GET /api/v1/cash-position/liquidity-forecasts/{id}
- PATCH /api/v1/cash-position/liquidity-forecasts/{id}
- POST /api/v1/cash-position/liquidity-forecasts/{id}/validate
- GET /api/v1/cash-position/shortfall-alerts
- POST /api/v1/cash-position/shortfall-alerts
- GET /api/v1/cash-position/shortfall-alerts/{id}
- PATCH /api/v1/cash-position/shortfall-alerts/{id}
- POST /api/v1/cash-position/shortfall-alerts/{id}/validate
- GET /api/v1/cash-position/mitigation-options
- POST /api/v1/cash-position/mitigation-options
- GET /api/v1/cash-position/mitigation-options/{id}
- PATCH /api/v1/cash-position/mitigation-options/{id}
- POST /api/v1/cash-position/mitigation-options/{id}/validate
- GET /api/v1/cash-position/scenario-runs
- POST /api/v1/cash-position/scenario-runs
- GET /api/v1/cash-position/scenario-runs/{id}
- PATCH /api/v1/cash-position/scenario-runs/{id}
- POST /api/v1/cash-position/scenario-runs/{id}/validate
- POST /api/v1/cash-position/ai/analyze
- POST /api/v1/cash-position/ai/recommendations
- POST /api/v1/cash-position/workflows/execute
- GET /api/v1/cash-position/health/contracts
- GET /api/v1/cash-position/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- positionsService: create/update/list/getById/delete/validate/simulate
- forecastsService: create/update/list/getById/delete/validate/simulate
- shortfallsService: create/update/list/getById/delete/validate/simulate
- mitigationsService: create/update/list/getById/delete/validate/simulate
- scenariosService: create/update/list/getById/delete/validate/simulate
- eventsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** cashflowai.position.updated, cashflowai.shortfall.detected, cashflowai.mitigation.recommended
- **Consumes:** invoiceprocessor.invoice.approved, budgetpilot.variance.alerted, debtnavigator.plan.approved
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
- Add backend service + controller tests for critical flows.
- Add frontend component/page tests for key user journeys.
- Add backend Dockerfile to make deploy path repeatable.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl cashflowai/backend clean verify`
- Frontend:
  - `npm --prefix cashflowai/frontend test`
  - `npm --prefix cashflowai/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
