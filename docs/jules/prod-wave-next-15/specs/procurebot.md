# ProcureBot — Next-15 Production Progression Spec

## Product Intent
- App: `procurebot`
- Domain: Finance & Procurement
- Outcome: Procurement workflow automation with approval tiers and vendor intelligence
- Primary actors: Procurement manager, Finance approver, Requestor

## Current Snapshot
- Score: 55
- Tier: Alpha
- Depth: Shallow
- Risks: no-backend-tests, no-frontend-tests, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add backend service/controller tests for critical paths.
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. requests: backend service + API + frontend page + tests
- [ ] 2. approvals: backend service + API + frontend page + tests
- [ ] 3. orders: backend service + API + frontend page + tests
- [ ] 4. offers: backend service + API + frontend page + tests
- [ ] 5. controls: backend service + API + frontend page + tests
- [ ] 6. events: backend service + API + frontend page + tests

## Domain Model
### PurchaseRequest
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PurchaseRequest display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ApprovalFlow
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ApprovalFlow display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### PurchaseOrder
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PurchaseOrder display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### VendorOffer
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | VendorOffer display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### SpendControlRule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SpendControlRule display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### ProcurementEvent
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | ProcurementEvent display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/procurement/purchase-requests
- POST /api/v1/procurement/purchase-requests
- GET /api/v1/procurement/purchase-requests/{id}
- PATCH /api/v1/procurement/purchase-requests/{id}
- POST /api/v1/procurement/purchase-requests/{id}/validate
- GET /api/v1/procurement/approval-flows
- POST /api/v1/procurement/approval-flows
- GET /api/v1/procurement/approval-flows/{id}
- PATCH /api/v1/procurement/approval-flows/{id}
- POST /api/v1/procurement/approval-flows/{id}/validate
- GET /api/v1/procurement/purchase-orders
- POST /api/v1/procurement/purchase-orders
- GET /api/v1/procurement/purchase-orders/{id}
- PATCH /api/v1/procurement/purchase-orders/{id}
- POST /api/v1/procurement/purchase-orders/{id}/validate
- GET /api/v1/procurement/vendor-offers
- POST /api/v1/procurement/vendor-offers
- GET /api/v1/procurement/vendor-offers/{id}
- PATCH /api/v1/procurement/vendor-offers/{id}
- POST /api/v1/procurement/vendor-offers/{id}/validate
- GET /api/v1/procurement/spend-control-rules
- POST /api/v1/procurement/spend-control-rules
- GET /api/v1/procurement/spend-control-rules/{id}
- PATCH /api/v1/procurement/spend-control-rules/{id}
- POST /api/v1/procurement/spend-control-rules/{id}/validate
- POST /api/v1/procurement/ai/analyze
- POST /api/v1/procurement/workflows/execute
- GET /api/v1/procurement/metrics/summary

## Event Contract
- Emits: procurebot.request.approved, procurebot.order.issued, procurebot.policy.blocked
- Consumes: vendoriq.risk.updated, budgetpilot.variance.alerted, policyforge.policy.published
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
- `mvn -pl procurebot/backend clean verify`
- `npm --prefix procurebot/frontend test`
- `npm --prefix procurebot/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `procurebot/.jules/HANDOFF.md`
- Submit PR when done
