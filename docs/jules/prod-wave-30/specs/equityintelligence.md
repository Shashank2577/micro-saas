# EquityIntelligence — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `equityintelligence`
- **Domain:** Finance Operations
- **Outcome:** Cap table, vesting, and round scenario intelligence for founders and finance teams
- **Primary actors:** Founder, Finance lead, Legal ops

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
- [ ] 1. cap-table: backend service + controller + frontend page + tests
- [ ] 2. shareholders: backend service + controller + frontend page + tests
- [ ] 3. vesting: backend service + controller + frontend page + tests
- [ ] 4. rounds: backend service + controller + frontend page + tests
- [ ] 5. dilution: backend service + controller + frontend page + tests
- [ ] 6. option-pool: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### CapTable
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | CapTable display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### Shareholder
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | Shareholder display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### VestingSchedule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | VestingSchedule display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### FundingRound
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | FundingRound display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### DilutionScenario
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | DilutionScenario display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### OptionPoolPlan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | OptionPoolPlan display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/equity/cap-tables
- POST /api/v1/equity/cap-tables
- GET /api/v1/equity/cap-tables/{id}
- PATCH /api/v1/equity/cap-tables/{id}
- POST /api/v1/equity/cap-tables/{id}/validate
- GET /api/v1/equity/shareholders
- POST /api/v1/equity/shareholders
- GET /api/v1/equity/shareholders/{id}
- PATCH /api/v1/equity/shareholders/{id}
- POST /api/v1/equity/shareholders/{id}/validate
- GET /api/v1/equity/vesting-schedules
- POST /api/v1/equity/vesting-schedules
- GET /api/v1/equity/vesting-schedules/{id}
- PATCH /api/v1/equity/vesting-schedules/{id}
- POST /api/v1/equity/vesting-schedules/{id}/validate
- GET /api/v1/equity/funding-rounds
- POST /api/v1/equity/funding-rounds
- GET /api/v1/equity/funding-rounds/{id}
- PATCH /api/v1/equity/funding-rounds/{id}
- POST /api/v1/equity/funding-rounds/{id}/validate
- GET /api/v1/equity/dilution-scenarios
- POST /api/v1/equity/dilution-scenarios
- GET /api/v1/equity/dilution-scenarios/{id}
- PATCH /api/v1/equity/dilution-scenarios/{id}
- POST /api/v1/equity/dilution-scenarios/{id}/validate
- POST /api/v1/equity/ai/analyze
- POST /api/v1/equity/ai/recommendations
- POST /api/v1/equity/workflows/execute
- GET /api/v1/equity/health/contracts
- GET /api/v1/equity/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- cap-tableService: create/update/list/getById/delete/validate/simulate
- shareholdersService: create/update/list/getById/delete/validate/simulate
- vestingService: create/update/list/getById/delete/validate/simulate
- roundsService: create/update/list/getById/delete/validate/simulate
- dilutionService: create/update/list/getById/delete/validate/simulate
- option-poolService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** equityintelligence.round.updated, equityintelligence.dilution.simulated, equityintelligence.vesting.alerted
- **Consumes:** financenarrator.narrative.generated, budgetpilot.reforecast.completed, contractportfolio.contract.indexed
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
  - `mvn -pl equityintelligence/backend clean verify`
- Frontend:
  - `npm --prefix equityintelligence/frontend test`
  - `npm --prefix equityintelligence/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
