# DataGovernanceOS — Next-15 Production Progression Spec

## Product Intent
- App: `datagovernanceos`
- Domain: Data Governance
- Outcome: Governance policy lifecycle, stewardship workflows, and compliance controls
- Primary actors: Data governance lead, Data steward, Compliance analyst

## Current Snapshot
- Score: 63
- Tier: Beta
- Depth: Shallow
- Risks: no-frontend-tests, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. policies: backend service + API + frontend page + tests
- [ ] 2. stewards: backend service + API + frontend page + tests
- [ ] 3. violations: backend service + API + frontend page + tests
- [ ] 4. exceptions: backend service + API + frontend page + tests
- [ ] 5. controls: backend service + API + frontend page + tests
- [ ] 6. audit: backend service + API + frontend page + tests

## Domain Model
### GovernancePolicy
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | GovernancePolicy display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### StewardAssignment
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | StewardAssignment display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### PolicyViolation
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PolicyViolation display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ExceptionRequest
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ExceptionRequest display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ControlCheck
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ControlCheck display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### AuditTrail
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | AuditTrail display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/data-governance/governance-policies
- POST /api/v1/data-governance/governance-policies
- GET /api/v1/data-governance/governance-policies/{id}
- PATCH /api/v1/data-governance/governance-policies/{id}
- POST /api/v1/data-governance/governance-policies/{id}/validate
- GET /api/v1/data-governance/steward-assignments
- POST /api/v1/data-governance/steward-assignments
- GET /api/v1/data-governance/steward-assignments/{id}
- PATCH /api/v1/data-governance/steward-assignments/{id}
- POST /api/v1/data-governance/steward-assignments/{id}/validate
- GET /api/v1/data-governance/policy-violations
- POST /api/v1/data-governance/policy-violations
- GET /api/v1/data-governance/policy-violations/{id}
- PATCH /api/v1/data-governance/policy-violations/{id}
- POST /api/v1/data-governance/policy-violations/{id}/validate
- GET /api/v1/data-governance/exception-requests
- POST /api/v1/data-governance/exception-requests
- GET /api/v1/data-governance/exception-requests/{id}
- PATCH /api/v1/data-governance/exception-requests/{id}
- POST /api/v1/data-governance/exception-requests/{id}/validate
- GET /api/v1/data-governance/control-checks
- POST /api/v1/data-governance/control-checks
- GET /api/v1/data-governance/control-checks/{id}
- PATCH /api/v1/data-governance/control-checks/{id}
- POST /api/v1/data-governance/control-checks/{id}/validate
- POST /api/v1/data-governance/ai/analyze
- POST /api/v1/data-governance/workflows/execute
- GET /api/v1/data-governance/metrics/summary

## Event Contract
- Emits: datagovernanceos.policy.updated, datagovernanceos.violation.detected, datagovernanceos.control.checked
- Consumes: datacatalogai.asset.registered, dataqualityai.rule.failed, auditready.gap.opened
- All events include: `event_id`, `tenant_id`, `occurred_at`, `source_app`, `payload`
- Consumers are idempotent by `event_id`

## Non-Functional Requirements
- P95 latency target: <300ms reads, <600ms writes
- Multi-tenant isolation on all reads/writes
- Structured logs with correlation IDs
- OpenAPI completeness and contract tests

## Acceptance Criteria
1. All module slices implemented with API + UI + tests.
2. Strict tenant isolation and RBAC checks on all endpoints.
3. Event emit/consume contracts implemented and validated.
4. OpenAPI includes all endpoints from this spec.
5. Backend and frontend test suites pass in CI.
6. LiteLLM calls are guarded with timeout/retry/circuit-breaker.
7. No TODO or stubbed production logic remains.

## Build & Verify
- `mvn -pl datagovernanceos/backend clean verify`
- `npm --prefix datagovernanceos/frontend test`
- `npm --prefix datagovernanceos/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `datagovernanceos/.jules/HANDOFF.md`
- Submit PR when done
