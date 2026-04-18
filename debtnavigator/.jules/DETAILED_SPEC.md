# DebtNavigator Detailed Spec

## 1) Overview
This document outlines the specific implementation details for the `debtnavigator` application as per the production progression spec.

## 2) Database Schema
```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE debt_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_debt_accounts_tenant_id ON debt_accounts(tenant_id);

CREATE TABLE payment_plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_payment_plans_tenant_id ON payment_plans(tenant_id);

CREATE TABLE optimization_runs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_optimization_runs_tenant_id ON optimization_runs(tenant_id);

CREATE TABLE consolidation_offers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_consolidation_offers_tenant_id ON consolidation_offers(tenant_id);

CREATE TABLE risk_projections (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_risk_projections_tenant_id ON risk_projections(tenant_id);

CREATE TABLE milestone_tracks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_milestone_tracks_tenant_id ON milestone_tracks(tenant_id);
```

## 3) API Endpoints
All endpoints are scoped by `X-Tenant-ID` header.

### Accounts
- `GET /api/v1/debt/debt-accounts` - List accounts
- `POST /api/v1/debt/debt-accounts` - Create account
- `GET /api/v1/debt/debt-accounts/{id}` - Get account
- `PATCH /api/v1/debt/debt-accounts/{id}` - Update account
- `POST /api/v1/debt/debt-accounts/{id}/validate` - Validate account (mocked logic)
- `POST /api/v1/debt/debt-accounts/{id}/simulate` - Simulate account (mocked logic)

### Plans
- `GET /api/v1/debt/payment-plans` - List plans
- `POST /api/v1/debt/payment-plans` - Create plan
- `GET /api/v1/debt/payment-plans/{id}` - Get plan
- `PATCH /api/v1/debt/payment-plans/{id}` - Update plan
- `POST /api/v1/debt/payment-plans/{id}/validate` - Validate plan
- `POST /api/v1/debt/payment-plans/{id}/simulate` - Simulate plan

### Optimization Runs
- `GET /api/v1/debt/optimization-runs` - List runs
- `POST /api/v1/debt/optimization-runs` - Create run
- `GET /api/v1/debt/optimization-runs/{id}` - Get run
- `PATCH /api/v1/debt/optimization-runs/{id}` - Update run
- `POST /api/v1/debt/optimization-runs/{id}/validate` - Validate run
- `POST /api/v1/debt/optimization-runs/{id}/simulate` - Simulate run

### Consolidation Offers
- `GET /api/v1/debt/consolidation-offers` - List offers
- `POST /api/v1/debt/consolidation-offers` - Create offer
- `GET /api/v1/debt/consolidation-offers/{id}` - Get offer
- `PATCH /api/v1/debt/consolidation-offers/{id}` - Update offer
- `POST /api/v1/debt/consolidation-offers/{id}/validate` - Validate offer
- `POST /api/v1/debt/consolidation-offers/{id}/simulate` - Simulate offer

### Risk Projections
- `GET /api/v1/debt/risk-projections` - List projections
- `POST /api/v1/debt/risk-projections` - Create projection
- `GET /api/v1/debt/risk-projections/{id}` - Get projection
- `PATCH /api/v1/debt/risk-projections/{id}` - Update projection
- `POST /api/v1/debt/risk-projections/{id}/validate` - Validate projection
- `POST /api/v1/debt/risk-projections/{id}/simulate` - Simulate projection

### Milestone Tracks
- `GET /api/v1/debt/milestone-tracks` - List milestones
- `POST /api/v1/debt/milestone-tracks` - Create milestone
- `GET /api/v1/debt/milestone-tracks/{id}` - Get milestone
- `PATCH /api/v1/debt/milestone-tracks/{id}` - Update milestone
- `POST /api/v1/debt/milestone-tracks/{id}/validate` - Validate milestone
- `POST /api/v1/debt/milestone-tracks/{id}/simulate` - Simulate milestone

### AI
- `POST /api/v1/debt/ai/analyze` - Analyze debt
- `POST /api/v1/debt/ai/recommendations` - Get recommendations

### Workflows
- `POST /api/v1/debt/workflows/execute` - Execute workflow

### Health & Metrics
- `GET /api/v1/debt/health/contracts` - Contracts health
- `GET /api/v1/debt/metrics/summary` - Metrics summary

## 4) Frontend Components
- `Dashboard`: Main entry point
- `AccountList`, `AccountDetail`, `AccountForm`
- `PlanList`, `PlanDetail`, `PlanForm`
- `OptimizationRunList`, `OptimizationRunDetail`, `OptimizationRunForm`
- `ConsolidationOfferList`, `ConsolidationOfferDetail`, `ConsolidationOfferForm`
- `RiskProjectionList`, `RiskProjectionDetail`, `RiskProjectionForm`
- `MilestoneTrackList`, `MilestoneTrackDetail`, `MilestoneTrackForm`

## 5) Error Handling
- Backend: Standard Spring `@ControllerAdvice` returning standard problem details format.
- Frontend: `ErrorBoundary` components and toast notifications for API failures.
