# RetentionSignal — Next-15 Production Progression Spec

## Overview
RetentionSignal is an application for HR Talent that provides employee flight-risk modeling and intervention recommendation engines.

## Database Schema

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE retention_risks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_retention_risks_tenant_id ON retention_risks(tenant_id);

CREATE TABLE intervention_plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_intervention_plans_tenant_id ON intervention_plans(tenant_id);

CREATE TABLE sentiment_signals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_sentiment_signals_tenant_id ON sentiment_signals(tenant_id);

CREATE TABLE risk_drivers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_risk_drivers_tenant_id ON risk_drivers(tenant_id);

CREATE TABLE followup_tasks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_followup_tasks_tenant_id ON followup_tasks(tenant_id);

CREATE TABLE outcome_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_outcome_records_tenant_id ON outcome_records(tenant_id);
```

## Endpoints
Every entity (RetentionRisk, InterventionPlan, SentimentSignal, RiskDriver, FollowupTask, OutcomeRecord) will have standard CRUD endpoints (GET list, POST create, GET by id, PATCH update).
The first 5 entities also have a POST `{id}/validate` endpoint.
There are also global endpoints: POST `/ai/analyze`, POST `/workflows/execute`, GET `/metrics/summary`.

All endpoints are under `/api/v1/retention`.
All operations isolate data by `X-Tenant-Id` header (tenant_id).

## Events
- Emits: retentionsignal.risk.detected, retentionsignal.intervention.recommended, retentionsignal.outcome.recorded
- Consumes: peopleanalytics.org.signal.updated, performancenarrative.review.finalized, engagementpulse.signal.updated

## UI Pages
- Dashboard `/`
- Retention Risks `/risks`
- Intervention Plans `/interventions`
- Sentiment Signals `/signals`
- Risk Drivers `/drivers`
- Followup Tasks `/followups`
- Outcomes `/outcomes`
