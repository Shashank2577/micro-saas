# SecurityPulse — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `securitypulse`
- **Domain:** Developer Security
- **Outcome:** Code and dependency security posture tracking with policy-enforced remediation
- **Primary actors:** Security engineer, Platform engineer, Engineering manager

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 40
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-frontend, no-backend-dockerfile
- **Low-hanging fruits before/while implementation:**
  - Add backend Dockerfile aligned with Java 21 and healthcheck.

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. scans: backend service + controller + frontend page + tests
- [ ] 2. findings: backend service + controller + frontend page + tests
- [ ] 3. policies: backend service + controller + frontend page + tests
- [ ] 4. remediation: backend service + controller + frontend page + tests
- [ ] 5. risk-trends: backend service + controller + frontend page + tests
- [ ] 6. exceptions: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### SecurityFinding
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SecurityFinding display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |
| severity | varchar(20) | CRITICAL/HIGH/MEDIUM/LOW |
| source | varchar(60) | Scanner source |
| rule_key | varchar(120) | Detection rule identifier |

### PolicyRule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | PolicyRule display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RepoScan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RepoScan display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RemediationTask
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RemediationTask display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RiskTrend
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RiskTrend display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SuppressionRecord
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SuppressionRecord display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/security/security-findings
- POST /api/v1/security/security-findings
- GET /api/v1/security/security-findings/{id}
- PATCH /api/v1/security/security-findings/{id}
- POST /api/v1/security/security-findings/{id}/validate
- GET /api/v1/security/policy-rules
- POST /api/v1/security/policy-rules
- GET /api/v1/security/policy-rules/{id}
- PATCH /api/v1/security/policy-rules/{id}
- POST /api/v1/security/policy-rules/{id}/validate
- GET /api/v1/security/repo-scans
- POST /api/v1/security/repo-scans
- GET /api/v1/security/repo-scans/{id}
- PATCH /api/v1/security/repo-scans/{id}
- POST /api/v1/security/repo-scans/{id}/validate
- GET /api/v1/security/remediation-tasks
- POST /api/v1/security/remediation-tasks
- GET /api/v1/security/remediation-tasks/{id}
- PATCH /api/v1/security/remediation-tasks/{id}
- POST /api/v1/security/remediation-tasks/{id}/validate
- GET /api/v1/security/risk-trends
- POST /api/v1/security/risk-trends
- GET /api/v1/security/risk-trends/{id}
- PATCH /api/v1/security/risk-trends/{id}
- POST /api/v1/security/risk-trends/{id}/validate
- POST /api/v1/security/ai/analyze
- POST /api/v1/security/ai/recommendations
- POST /api/v1/security/workflows/execute
- GET /api/v1/security/health/contracts
- GET /api/v1/security/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- scansService: create/update/list/getById/delete/validate/simulate
- findingsService: create/update/list/getById/delete/validate/simulate
- policiesService: create/update/list/getById/delete/validate/simulate
- remediationService: create/update/list/getById/delete/validate/simulate
- risk-trendsService: create/update/list/getById/delete/validate/simulate
- exceptionsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** securitypulse.finding.created, securitypulse.policy.violation, securitypulse.finding.resolved
- **Consumes:** dependencyradar.vulnerability.detected, deploysignal.deployment.started, apigatekeeper.policy.changed
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

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl securitypulse/backend clean verify`
- Frontend:
  - `npm --prefix securitypulse/frontend test`
  - `npm --prefix securitypulse/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
