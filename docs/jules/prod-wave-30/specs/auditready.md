# AuditReady — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `auditready`
- **Domain:** Compliance Automation
- **Outcome:** Evidence collection, control mapping, and readiness scoring for SOC2/ISO/GDPR
- **Primary actors:** Compliance lead, Security lead, Auditor liaison

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 45
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-frontend, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. frameworks: backend service + controller + frontend page + tests
- [ ] 2. controls: backend service + controller + frontend page + tests
- [ ] 3. evidence: backend service + controller + frontend page + tests
- [ ] 4. scoring: backend service + controller + frontend page + tests
- [ ] 5. gaps: backend service + controller + frontend page + tests
- [ ] 6. remediation: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### Control
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | Control display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### EvidenceItem
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | EvidenceItem display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### AuditFramework
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | AuditFramework display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ReadinessScore
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ReadinessScore display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### GapIssue
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | GapIssue display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RemediationPlan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RemediationPlan display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/audits/controls
- POST /api/v1/audits/controls
- GET /api/v1/audits/controls/{id}
- PATCH /api/v1/audits/controls/{id}
- POST /api/v1/audits/controls/{id}/validate
- GET /api/v1/audits/evidence-items
- POST /api/v1/audits/evidence-items
- GET /api/v1/audits/evidence-items/{id}
- PATCH /api/v1/audits/evidence-items/{id}
- POST /api/v1/audits/evidence-items/{id}/validate
- GET /api/v1/audits/audit-frameworks
- POST /api/v1/audits/audit-frameworks
- GET /api/v1/audits/audit-frameworks/{id}
- PATCH /api/v1/audits/audit-frameworks/{id}
- POST /api/v1/audits/audit-frameworks/{id}/validate
- GET /api/v1/audits/readiness-scores
- POST /api/v1/audits/readiness-scores
- GET /api/v1/audits/readiness-scores/{id}
- PATCH /api/v1/audits/readiness-scores/{id}
- POST /api/v1/audits/readiness-scores/{id}/validate
- GET /api/v1/audits/gap-issues
- POST /api/v1/audits/gap-issues
- GET /api/v1/audits/gap-issues/{id}
- PATCH /api/v1/audits/gap-issues/{id}
- POST /api/v1/audits/gap-issues/{id}/validate
- POST /api/v1/audits/ai/analyze
- POST /api/v1/audits/ai/recommendations
- POST /api/v1/audits/workflows/execute
- GET /api/v1/audits/health/contracts
- GET /api/v1/audits/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- frameworksService: create/update/list/getById/delete/validate/simulate
- controlsService: create/update/list/getById/delete/validate/simulate
- evidenceService: create/update/list/getById/delete/validate/simulate
- scoringService: create/update/list/getById/delete/validate/simulate
- gapsService: create/update/list/getById/delete/validate/simulate
- remediationService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** auditready.evidence.collected, auditready.readiness.scored, auditready.gap.opened
- **Consumes:** policyforge.policy.published, complianceradar.change.detected, licenseguard.violation.detected
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
  - `mvn -pl auditready/backend clean verify`
- Frontend:
  - `npm --prefix auditready/frontend test`
  - `npm --prefix auditready/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
