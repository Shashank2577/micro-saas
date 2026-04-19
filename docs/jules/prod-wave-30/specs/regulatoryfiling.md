# RegulatoryFiling — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `regulatoryfiling`
- **Domain:** Compliance Automation
- **Outcome:** Regulatory filing calendar, obligation tracking, and document assembly
- **Primary actors:** Compliance manager, Legal ops, Controller

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 50
- **Tier:** Alpha
- **Depth:** Medium
- **Known structural risks:** no-frontend, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. obligations: backend service + controller + frontend page + tests
- [ ] 2. calendar: backend service + controller + frontend page + tests
- [ ] 3. packets: backend service + controller + frontend page + tests
- [ ] 4. validations: backend service + controller + frontend page + tests
- [ ] 5. submissions: backend service + controller + frontend page + tests
- [ ] 6. receipts: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### FilingObligation
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | FilingObligation display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### JurisdictionSchedule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | JurisdictionSchedule display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SubmissionPacket
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SubmissionPacket display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### FilingDeadline
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | FilingDeadline display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ValidationCheck
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ValidationCheck display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SubmissionReceipt
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SubmissionReceipt display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/filings/filing-obligations
- POST /api/v1/filings/filing-obligations
- GET /api/v1/filings/filing-obligations/{id}
- PATCH /api/v1/filings/filing-obligations/{id}
- POST /api/v1/filings/filing-obligations/{id}/validate
- GET /api/v1/filings/jurisdiction-schedules
- POST /api/v1/filings/jurisdiction-schedules
- GET /api/v1/filings/jurisdiction-schedules/{id}
- PATCH /api/v1/filings/jurisdiction-schedules/{id}
- POST /api/v1/filings/jurisdiction-schedules/{id}/validate
- GET /api/v1/filings/submission-packets
- POST /api/v1/filings/submission-packets
- GET /api/v1/filings/submission-packets/{id}
- PATCH /api/v1/filings/submission-packets/{id}
- POST /api/v1/filings/submission-packets/{id}/validate
- GET /api/v1/filings/filing-deadlines
- POST /api/v1/filings/filing-deadlines
- GET /api/v1/filings/filing-deadlines/{id}
- PATCH /api/v1/filings/filing-deadlines/{id}
- POST /api/v1/filings/filing-deadlines/{id}/validate
- GET /api/v1/filings/validation-checks
- POST /api/v1/filings/validation-checks
- GET /api/v1/filings/validation-checks/{id}
- PATCH /api/v1/filings/validation-checks/{id}
- POST /api/v1/filings/validation-checks/{id}/validate
- POST /api/v1/filings/ai/analyze
- POST /api/v1/filings/ai/recommendations
- POST /api/v1/filings/workflows/execute
- GET /api/v1/filings/health/contracts
- GET /api/v1/filings/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- obligationsService: create/update/list/getById/delete/validate/simulate
- calendarService: create/update/list/getById/delete/validate/simulate
- packetsService: create/update/list/getById/delete/validate/simulate
- validationsService: create/update/list/getById/delete/validate/simulate
- submissionsService: create/update/list/getById/delete/validate/simulate
- receiptsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** regulatoryfiling.deadline.alerted, regulatoryfiling.packet.ready, regulatoryfiling.filing.submitted
- **Consumes:** complianceradar.change.detected, policyforge.policy.published, auditready.gap.opened
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
- Add backend Dockerfile to make deploy path repeatable.
- Add frontend app shell for operator workflows and observability.
- Add app README with setup, contracts, SLOs, and runbooks.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl regulatoryfiling/backend clean verify`
- Frontend:
  - `npm --prefix regulatoryfiling/frontend test`
  - `npm --prefix regulatoryfiling/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
