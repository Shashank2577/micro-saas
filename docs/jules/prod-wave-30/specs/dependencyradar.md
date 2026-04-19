# DependencyRadar — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `dependencyradar`
- **Domain:** Developer Security
- **Outcome:** Dependency inventory, CVE detection, and remediation orchestration
- **Primary actors:** Security engineer, Dev lead, Platform owner

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 45
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-frontend, no-db-migrations, no-backend-tests, no-backend-dockerfile
- **Low-hanging fruits before/while implementation:**
  - Add backend unit + integration test suite for service and controller paths.
  - Introduce Flyway V1 baseline and follow-up incremental migrations.
  - Add backend Dockerfile aligned with Java 21 and healthcheck.

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. inventory: backend service + controller + frontend page + tests
- [ ] 2. scans: backend service + controller + frontend page + tests
- [ ] 3. vulnerabilities: backend service + controller + frontend page + tests
- [ ] 4. upgrades: backend service + controller + frontend page + tests
- [ ] 5. exceptions: backend service + controller + frontend page + tests
- [ ] 6. analytics: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### DependencyAsset
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | DependencyAsset display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### Vulnerability
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | Vulnerability display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### UpgradePlan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | UpgradePlan display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ExposureScore
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ExposureScore display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### PolicyException
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | PolicyException display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ScanRun
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ScanRun display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/dependencies/dependency-assets
- POST /api/v1/dependencies/dependency-assets
- GET /api/v1/dependencies/dependency-assets/{id}
- PATCH /api/v1/dependencies/dependency-assets/{id}
- POST /api/v1/dependencies/dependency-assets/{id}/validate
- GET /api/v1/dependencies/vulnerabilitys
- POST /api/v1/dependencies/vulnerabilitys
- GET /api/v1/dependencies/vulnerabilitys/{id}
- PATCH /api/v1/dependencies/vulnerabilitys/{id}
- POST /api/v1/dependencies/vulnerabilitys/{id}/validate
- GET /api/v1/dependencies/upgrade-plans
- POST /api/v1/dependencies/upgrade-plans
- GET /api/v1/dependencies/upgrade-plans/{id}
- PATCH /api/v1/dependencies/upgrade-plans/{id}
- POST /api/v1/dependencies/upgrade-plans/{id}/validate
- GET /api/v1/dependencies/exposure-scores
- POST /api/v1/dependencies/exposure-scores
- GET /api/v1/dependencies/exposure-scores/{id}
- PATCH /api/v1/dependencies/exposure-scores/{id}
- POST /api/v1/dependencies/exposure-scores/{id}/validate
- GET /api/v1/dependencies/policy-exceptions
- POST /api/v1/dependencies/policy-exceptions
- GET /api/v1/dependencies/policy-exceptions/{id}
- PATCH /api/v1/dependencies/policy-exceptions/{id}
- POST /api/v1/dependencies/policy-exceptions/{id}/validate
- POST /api/v1/dependencies/ai/analyze
- POST /api/v1/dependencies/ai/recommendations
- POST /api/v1/dependencies/workflows/execute
- GET /api/v1/dependencies/health/contracts
- GET /api/v1/dependencies/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- inventoryService: create/update/list/getById/delete/validate/simulate
- scansService: create/update/list/getById/delete/validate/simulate
- vulnerabilitiesService: create/update/list/getById/delete/validate/simulate
- upgradesService: create/update/list/getById/delete/validate/simulate
- exceptionsService: create/update/list/getById/delete/validate/simulate
- analyticsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** dependencyradar.vulnerability.detected, dependencyradar.upgrade.recommended, dependencyradar.scan.completed
- **Consumes:** securitypulse.policy.violation, apimanager.version.published, deploysignal.deployment.risk.scored
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
- Add Flyway baseline and incremental migrations for all persisted entities.
- Add backend service + controller tests for critical flows.
- Add backend Dockerfile to make deploy path repeatable.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl dependencyradar/backend clean verify`
- Frontend:
  - `npm --prefix dependencyradar/frontend test`
  - `npm --prefix dependencyradar/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
