# ComplianceRadar — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `complianceradar`
- **Domain:** Compliance Automation
- **Outcome:** Monitor regulatory changes and map deltas to internal controls and policies
- **Primary actors:** Compliance analyst, Risk manager, Security lead

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 50
- **Tier:** Alpha
- **Depth:** Medium
- **Known structural risks:** no-frontend, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. feeds: backend service + controller + frontend page + tests
- [ ] 2. normalization: backend service + controller + frontend page + tests
- [ ] 3. impact-assessment: backend service + controller + frontend page + tests
- [ ] 4. gap-mapping: backend service + controller + frontend page + tests
- [ ] 5. tasks: backend service + controller + frontend page + tests
- [ ] 6. alerts: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### RegulationUpdate
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RegulationUpdate display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### JurisdictionRule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | JurisdictionRule display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ImpactAssessment
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ImpactAssessment display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ControlGap
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ControlGap display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### TaskAssignment
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | TaskAssignment display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### DeadlineAlert
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | DeadlineAlert display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/regulations/regulation-updates
- POST /api/v1/regulations/regulation-updates
- GET /api/v1/regulations/regulation-updates/{id}
- PATCH /api/v1/regulations/regulation-updates/{id}
- POST /api/v1/regulations/regulation-updates/{id}/validate
- GET /api/v1/regulations/jurisdiction-rules
- POST /api/v1/regulations/jurisdiction-rules
- GET /api/v1/regulations/jurisdiction-rules/{id}
- PATCH /api/v1/regulations/jurisdiction-rules/{id}
- POST /api/v1/regulations/jurisdiction-rules/{id}/validate
- GET /api/v1/regulations/impact-assessments
- POST /api/v1/regulations/impact-assessments
- GET /api/v1/regulations/impact-assessments/{id}
- PATCH /api/v1/regulations/impact-assessments/{id}
- POST /api/v1/regulations/impact-assessments/{id}/validate
- GET /api/v1/regulations/control-gaps
- POST /api/v1/regulations/control-gaps
- GET /api/v1/regulations/control-gaps/{id}
- PATCH /api/v1/regulations/control-gaps/{id}
- POST /api/v1/regulations/control-gaps/{id}/validate
- GET /api/v1/regulations/task-assignments
- POST /api/v1/regulations/task-assignments
- GET /api/v1/regulations/task-assignments/{id}
- PATCH /api/v1/regulations/task-assignments/{id}
- POST /api/v1/regulations/task-assignments/{id}/validate
- POST /api/v1/regulations/ai/analyze
- POST /api/v1/regulations/ai/recommendations
- POST /api/v1/regulations/workflows/execute
- GET /api/v1/regulations/health/contracts
- GET /api/v1/regulations/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- feedsService: create/update/list/getById/delete/validate/simulate
- normalizationService: create/update/list/getById/delete/validate/simulate
- impact-assessmentService: create/update/list/getById/delete/validate/simulate
- gap-mappingService: create/update/list/getById/delete/validate/simulate
- tasksService: create/update/list/getById/delete/validate/simulate
- alertsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** complianceradar.change.detected, complianceradar.gap.opened, complianceradar.deadline.alerted
- **Consumes:** policyforge.policy.published, auditready.readiness.scored, regulatoryfiling.filing.submitted
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
  - `mvn -pl complianceradar/backend clean verify`
- Frontend:
  - `npm --prefix complianceradar/frontend test`
  - `npm --prefix complianceradar/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
