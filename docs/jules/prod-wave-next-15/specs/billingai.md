# BillingAI — Next-15 Production Progression Spec

## Product Intent
- App: `billingai`
- Domain: Revenue Operations
- Outcome: Subscription billing orchestration, dunning, and revenue leakage prevention
- Primary actors: RevOps, Finance manager, Support billing specialist

## Current Snapshot
- Score: 60
- Tier: Beta
- Depth: Shallow
- Risks: no-frontend-tests, no-backend-dockerfile, no-readme
- Low-hanging fruits:
  - Add frontend tests for primary page and mutation flow.
  - Add backend Dockerfile with Java 21 runtime.
  - Add operational README with setup, env vars, and runbook.

## Scope (must be complete in one Jules session)
- [ ] 1. plans: backend service + API + frontend page + tests
- [ ] 2. invoice-runs: backend service + API + frontend page + tests
- [ ] 3. dunning: backend service + API + frontend page + tests
- [ ] 4. payments: backend service + API + frontend page + tests
- [ ] 5. leakage: backend service + API + frontend page + tests
- [ ] 6. tax: backend service + API + frontend page + tests

## Domain Model
### SubscriptionPlan
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | SubscriptionPlan display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### InvoiceRun
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | InvoiceRun display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### DunningFlow
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | DunningFlow display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### PaymentAttempt
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | PaymentAttempt display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### RevenueLeakAlert
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | RevenueLeakAlert display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |

### TaxRule
| Field | Type | Notes |
|---|---|---|
| id | UUID | Primary key |
| tenant_id | UUID | Tenant key |
| name | varchar(180) | TaxRule display name |
| status | varchar(40) | Lifecycle enum |
| metadata_json | jsonb | Extensible attributes |
| created_at | timestamptz | Creation time |
| updated_at | timestamptz | Last update time |


## Required Endpoints
- GET /api/v1/billing/subscription-plans
- POST /api/v1/billing/subscription-plans
- GET /api/v1/billing/subscription-plans/{id}
- PATCH /api/v1/billing/subscription-plans/{id}
- POST /api/v1/billing/subscription-plans/{id}/validate
- GET /api/v1/billing/invoice-runs
- POST /api/v1/billing/invoice-runs
- GET /api/v1/billing/invoice-runs/{id}
- PATCH /api/v1/billing/invoice-runs/{id}
- POST /api/v1/billing/invoice-runs/{id}/validate
- GET /api/v1/billing/dunning-flows
- POST /api/v1/billing/dunning-flows
- GET /api/v1/billing/dunning-flows/{id}
- PATCH /api/v1/billing/dunning-flows/{id}
- POST /api/v1/billing/dunning-flows/{id}/validate
- GET /api/v1/billing/payment-attempts
- POST /api/v1/billing/payment-attempts
- GET /api/v1/billing/payment-attempts/{id}
- PATCH /api/v1/billing/payment-attempts/{id}
- POST /api/v1/billing/payment-attempts/{id}/validate
- GET /api/v1/billing/revenue-leak-alerts
- POST /api/v1/billing/revenue-leak-alerts
- GET /api/v1/billing/revenue-leak-alerts/{id}
- PATCH /api/v1/billing/revenue-leak-alerts/{id}
- POST /api/v1/billing/revenue-leak-alerts/{id}/validate
- POST /api/v1/billing/ai/analyze
- POST /api/v1/billing/workflows/execute
- GET /api/v1/billing/metrics/summary

## Event Contract
- Emits: billingai.invoice.generated, billingai.payment.failed, billingai.dunning.completed
- Consumes: usageintelligence.event.received, apimanager.key.generated, notificationhub.channel.registered
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
- `mvn -pl billingai/backend clean verify`
- `npm --prefix billingai/frontend test`
- `npm --prefix billingai/frontend run build`

## Autonomous Constraints for Jules
- Work fully autonomously with no feedback loop
- Commit and push after each phase
- Document assumptions in `billingai/.jules/HANDOFF.md`
- Submit PR when done
