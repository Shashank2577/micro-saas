# ProcureBot Detailed Specification

## Domain Model & Database Schema

```sql
CREATE TABLE purchase_requests (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE approval_flows (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE purchase_orders (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE vendor_offers (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE spend_control_rules (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE procurement_events (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
```

## Endpoints
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

## AI Integration
LiteLLM integration with a client to analyze procurement details.

## Events
- Emits: procurebot.request.approved, procurebot.order.issued, procurebot.policy.blocked
- Consumes: vendoriq.risk.updated, budgetpilot.variance.alerted, policyforge.policy.published
