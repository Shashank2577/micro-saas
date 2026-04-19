# LicenseGuard — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `licenseguard`
- **Domain:** Compliance Automation
- **Outcome:** OSS license governance and usage policy enforcement across repositories
- **Primary actors:** Legal ops, Security team, Engineering lead

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 45
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-frontend, no-backend-dockerfile, no-readme
- **Low-hanging fruits before/while implementation:**
  - Add backend Dockerfile aligned with Java 21 and healthcheck.
  - Publish operational README (runbook, env vars, known failure modes).

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. inventory: backend service + controller + frontend page + tests
- [ ] 2. rules: backend service + controller + frontend page + tests
- [ ] 3. issues: backend service + controller + frontend page + tests
- [ ] 4. attribution: backend service + controller + frontend page + tests
- [ ] 5. approvals: backend service + controller + frontend page + tests
- [ ] 6. sbom: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### LicenseAsset
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | LicenseAsset display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### LicenseRule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | LicenseRule display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ComplianceIssue
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ComplianceIssue display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### AttributionRecord
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | AttributionRecord display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ApprovalRequest
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ApprovalRequest display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### SBOMSnapshot
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | SBOMSnapshot display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/licenses/license-assets
- POST /api/v1/licenses/license-assets
- GET /api/v1/licenses/license-assets/{id}
- PATCH /api/v1/licenses/license-assets/{id}
- POST /api/v1/licenses/license-assets/{id}/validate
- GET /api/v1/licenses/license-rules
- POST /api/v1/licenses/license-rules
- GET /api/v1/licenses/license-rules/{id}
- PATCH /api/v1/licenses/license-rules/{id}
- POST /api/v1/licenses/license-rules/{id}/validate
- GET /api/v1/licenses/compliance-issues
- POST /api/v1/licenses/compliance-issues
- GET /api/v1/licenses/compliance-issues/{id}
- PATCH /api/v1/licenses/compliance-issues/{id}
- POST /api/v1/licenses/compliance-issues/{id}/validate
- GET /api/v1/licenses/attribution-records
- POST /api/v1/licenses/attribution-records
- GET /api/v1/licenses/attribution-records/{id}
- PATCH /api/v1/licenses/attribution-records/{id}
- POST /api/v1/licenses/attribution-records/{id}/validate
- GET /api/v1/licenses/approval-requests
- POST /api/v1/licenses/approval-requests
- GET /api/v1/licenses/approval-requests/{id}
- PATCH /api/v1/licenses/approval-requests/{id}
- POST /api/v1/licenses/approval-requests/{id}/validate
- POST /api/v1/licenses/ai/analyze
- POST /api/v1/licenses/ai/recommendations
- POST /api/v1/licenses/workflows/execute
- GET /api/v1/licenses/health/contracts
- GET /api/v1/licenses/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- inventoryService: create/update/list/getById/delete/validate/simulate
- rulesService: create/update/list/getById/delete/validate/simulate
- issuesService: create/update/list/getById/delete/validate/simulate
- attributionService: create/update/list/getById/delete/validate/simulate
- approvalsService: create/update/list/getById/delete/validate/simulate
- sbomService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** licenseguard.issue.detected, licenseguard.policy.updated, licenseguard.sbom.generated
- **Consumes:** dependencyradar.vulnerability.detected, auditready.framework.updated, policyforge.policy.published
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
  - `mvn -pl licenseguard/backend clean verify`
- Frontend:
  - `npm --prefix licenseguard/frontend test`
  - `npm --prefix licenseguard/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
