# DeploySignal — Production Progression Spec (Wave Upgrade)

## 1) Product Intent
- **App:** `deploysignal`
- **Domain:** Developer Intelligence
- **Outcome:** DORA metrics, deployment risk scoring, and release guardrails
- **Primary actors:** Platform engineer, Engineering manager, Release manager

## 2) Current Maturity Snapshot (from repository audit)
- **Score:** 48
- **Tier:** Alpha
- **Depth:** Shallow
- **Known structural risks:** no-db-migrations, no-frontend-tests, no-backend-dockerfile
- **Low-hanging fruits before/while implementation:**
  - Add frontend component and page interaction tests with Vitest + Testing Library.
  - Introduce Flyway V1 baseline and follow-up incremental migrations.
  - Add backend Dockerfile aligned with Java 21 and healthcheck.

## 3) Scope for This Jules Session (must be fully implemented)
- [ ] 1. deployments: backend service + controller + frontend page + tests
- [ ] 2. risk-scoring: backend service + controller + frontend page + tests
- [ ] 3. dora-metrics: backend service + controller + frontend page + tests
- [ ] 4. rollbacks: backend service + controller + frontend page + tests
- [ ] 5. windows: backend service + controller + frontend page + tests
- [ ] 6. reports: backend service + controller + frontend page + tests

## 4) Domain Model (authoritative object design)
### Deployment
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | Deployment display name |
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

### DoraMetricSnapshot
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | DoraMetricSnapshot display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### IncidentLink
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | IncidentLink display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### RollbackEvent
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | RollbackEvent display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |

### ReleaseWindow
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant isolation key |
| name | varchar(180) | ReleaseWindow display name |
| status | varchar(40) | Lifecycle status enum |
| metadata_json | jsonb | Extensible structured attributes |
| created_at | timestamptz | Creation timestamp |
| updated_at | timestamptz | Last updated timestamp |


## 5) API Contract (minimum required endpoints)
- GET /api/v1/deployments/deployments
- POST /api/v1/deployments/deployments
- GET /api/v1/deployments/deployments/{id}
- PATCH /api/v1/deployments/deployments/{id}
- POST /api/v1/deployments/deployments/{id}/validate
- GET /api/v1/deployments/risk-scores
- POST /api/v1/deployments/risk-scores
- GET /api/v1/deployments/risk-scores/{id}
- PATCH /api/v1/deployments/risk-scores/{id}
- POST /api/v1/deployments/risk-scores/{id}/validate
- GET /api/v1/deployments/dora-metric-snapshots
- POST /api/v1/deployments/dora-metric-snapshots
- GET /api/v1/deployments/dora-metric-snapshots/{id}
- PATCH /api/v1/deployments/dora-metric-snapshots/{id}
- POST /api/v1/deployments/dora-metric-snapshots/{id}/validate
- GET /api/v1/deployments/incident-links
- POST /api/v1/deployments/incident-links
- GET /api/v1/deployments/incident-links/{id}
- PATCH /api/v1/deployments/incident-links/{id}
- POST /api/v1/deployments/incident-links/{id}/validate
- GET /api/v1/deployments/rollback-events
- POST /api/v1/deployments/rollback-events
- GET /api/v1/deployments/rollback-events/{id}
- PATCH /api/v1/deployments/rollback-events/{id}
- POST /api/v1/deployments/rollback-events/{id}/validate
- POST /api/v1/deployments/ai/analyze
- POST /api/v1/deployments/ai/recommendations
- POST /api/v1/deployments/workflows/execute
- GET /api/v1/deployments/health/contracts
- GET /api/v1/deployments/metrics/summary

## 6) Service Layer Contract
Implement the following service modules with explicit method-level unit tests:
- deploymentsService: create/update/list/getById/delete/validate/simulate
- risk-scoringService: create/update/list/getById/delete/validate/simulate
- dora-metricsService: create/update/list/getById/delete/validate/simulate
- rollbacksService: create/update/list/getById/delete/validate/simulate
- windowsService: create/update/list/getById/delete/validate/simulate
- reportsService: create/update/list/getById/delete/validate/simulate

## 7) Event & Integration Contract
- **Emits:** deploysignal.deployment.risk.scored, deploysignal.rollback.detected, deploysignal.dora.updated
- **Consumes:** incidentbrain.incident.opened, observabilitystack.alert.triggered, securitypulse.finding.created
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
- Add Flyway baseline and incremental migrations for all persisted entities.
- Add frontend component/page tests for key user journeys.
- Add backend Dockerfile to make deploy path repeatable.

## 13) Explicit Build/Verification Commands
- Backend:
  - `mvn -pl deploysignal/backend clean verify`
- Frontend:
  - `npm --prefix deploysignal/frontend test`
  - `npm --prefix deploysignal/frontend run build`
- Contract checks:
  - Validate integration manifest against implemented events
  - Validate OpenAPI endpoint completeness

## 14) Autonomous Execution Constraints for Jules
- Work **autonomously**: no questions back to chat.
- Do full implementation in one session with phase commits.
- If ambiguous, choose a sensible default, document in HANDOFF.md, and continue.
- Push after each phase to prevent timeout-related loss.
- Submit PR at the end; do not block on approvals.
