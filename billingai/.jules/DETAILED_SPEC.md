# BillingAI — Detailed Specification

## Product Intent
- **App**: `billingai`
- **Domain**: Revenue Operations
- **Outcome**: Subscription billing orchestration, dunning, and revenue leakage prevention
- **Primary actors**: RevOps, Finance manager, Support billing specialist

## Database Schema

```sql
CREATE TABLE subscription_plans (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE invoice_runs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE dunning_flows (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE payment_attempts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE revenue_leak_alerts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE tax_rules (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
```

## REST Endpoints

### Subscription Plans
- `GET /api/v1/billing/subscription-plans` - List plans for tenant
- `POST /api/v1/billing/subscription-plans` - Create a plan
- `GET /api/v1/billing/subscription-plans/{id}` - Get plan details
- `PATCH /api/v1/billing/subscription-plans/{id}` - Update plan details
- `POST /api/v1/billing/subscription-plans/{id}/validate` - Validate plan configuration

### Invoice Runs
- `GET /api/v1/billing/invoice-runs` - List invoice runs
- `POST /api/v1/billing/invoice-runs` - Create invoice run
- `GET /api/v1/billing/invoice-runs/{id}` - Get run details
- `PATCH /api/v1/billing/invoice-runs/{id}` - Update run
- `POST /api/v1/billing/invoice-runs/{id}/validate` - Validate run

### Dunning Flows
- `GET /api/v1/billing/dunning-flows` - List dunning flows
- `POST /api/v1/billing/dunning-flows` - Create dunning flow
- `GET /api/v1/billing/dunning-flows/{id}` - Get flow details
- `PATCH /api/v1/billing/dunning-flows/{id}` - Update flow
- `POST /api/v1/billing/dunning-flows/{id}/validate` - Validate flow

### Payment Attempts
- `GET /api/v1/billing/payment-attempts` - List payment attempts
- `POST /api/v1/billing/payment-attempts` - Create attempt record
- `GET /api/v1/billing/payment-attempts/{id}` - Get attempt details
- `PATCH /api/v1/billing/payment-attempts/{id}` - Update attempt
- `POST /api/v1/billing/payment-attempts/{id}/validate` - Validate attempt

### Revenue Leak Alerts
- `GET /api/v1/billing/revenue-leak-alerts` - List alerts
- `POST /api/v1/billing/revenue-leak-alerts` - Create alert
- `GET /api/v1/billing/revenue-leak-alerts/{id}` - Get alert details
- `PATCH /api/v1/billing/revenue-leak-alerts/{id}` - Update alert
- `POST /api/v1/billing/revenue-leak-alerts/{id}/validate` - Validate alert

### Tax Rules
- `GET /api/v1/billing/tax-rules`
- `POST /api/v1/billing/tax-rules`
- `GET /api/v1/billing/tax-rules/{id}`
- `PATCH /api/v1/billing/tax-rules/{id}`

### AI Endpoints
- `POST /api/v1/billing/ai/analyze` - Analyzes billing data
- `POST /api/v1/billing/workflows/execute` - Execute workflow using AI
- `GET /api/v1/billing/metrics/summary` - Metrics summary

## Service Signatures
For each entity (e.g. `SubscriptionPlan`), the service interface includes:
- `List<Entity> findAll()`
- `Entity findById(UUID id)`
- `Entity create(Entity entity)`
- `Entity update(UUID id, Entity entity)`
- `boolean validate(UUID id)`

AI Service:
- `String analyze(String request)`
- `String executeWorkflow(String request)`
- `String getSummary()`

## React Components / Pages
- `/plans`
- `/invoice-runs`
- `/dunning`
- `/payments`
- `/leakage`
- `/tax`

## Event Contract
- **Emits**: `billingai.invoice.generated`, `billingai.payment.failed`, `billingai.dunning.completed`
- **Consumes**: `usageintelligence.event.received`, `apimanager.key.generated`, `notificationhub.channel.registered`

## Non-Functional
- Tenant isolation
- API key/RBAC checks
- OpenAPI documentation
- Tests (Backend >80%, Frontend vitest/rtl)
