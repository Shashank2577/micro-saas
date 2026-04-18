# PeopleAnalytics — Detailed Specification

## 1. Database Schema
```sql
CREATE TABLE IF NOT EXISTS org_snapshots (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_org_snapshots_tenant ON org_snapshots(tenant_id);

CREATE TABLE IF NOT EXISTS headcount_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_headcount_metrics_tenant ON headcount_metrics(tenant_id);

CREATE TABLE IF NOT EXISTS attrition_signals (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_attrition_signals_tenant ON attrition_signals(tenant_id);

CREATE TABLE IF NOT EXISTS engagement_indicators (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_engagement_indicators_tenant ON engagement_indicators(tenant_id);

CREATE TABLE IF NOT EXISTS performance_trends (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_performance_trends_tenant ON performance_trends(tenant_id);

CREATE TABLE IF NOT EXISTS planning_scenarios (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_planning_scenarios_tenant ON planning_scenarios(tenant_id);
```

## 2. API Endpoints
All endpoints are prefixed with `/api/v1/people-analytics`.
They must ensure multi-tenant isolation by using a `X-Tenant-ID` header.

### Org Snapshots
- `GET /org-snapshots` - List org snapshots for tenant
- `POST /org-snapshots` - Create org snapshot
- `GET /org-snapshots/{id}` - Get org snapshot by ID
- `PATCH /org-snapshots/{id}` - Update org snapshot
- `POST /org-snapshots/{id}/validate` - Validate org snapshot logic

### Headcount Metrics
- `GET /headcount-metrics`
- `POST /headcount-metrics`
- `GET /headcount-metrics/{id}`
- `PATCH /headcount-metrics/{id}`
- `POST /headcount-metrics/{id}/validate`

### Attrition Signals
- `GET /attrition-signals`
- `POST /attrition-signals`
- `GET /attrition-signals/{id}`
- `PATCH /attrition-signals/{id}`
- `POST /attrition-signals/{id}/validate`

### Engagement Indicators
- `GET /engagement-indicators`
- `POST /engagement-indicators`
- `GET /engagement-indicators/{id}`
- `PATCH /engagement-indicators/{id}`
- `POST /engagement-indicators/{id}/validate`

### Performance Trends
- `GET /performance-trends`
- `POST /performance-trends`
- `GET /performance-trends/{id}`
- `PATCH /performance-trends/{id}`
- `POST /performance-trends/{id}/validate`

### Planning Scenarios (Added for completeness based on domain model)
- `GET /planning-scenarios`
- `POST /planning-scenarios`
- `GET /planning-scenarios/{id}`
- `PATCH /planning-scenarios/{id}`
- `POST /planning-scenarios/{id}/validate`

### Global Endpoints
- `POST /ai/analyze` - Request payload: `{ "query": "string", "context": "object" }`
- `POST /workflows/execute` - Request payload: `{ "workflow_id": "string", "parameters": "object" }`
- `GET /metrics/summary` - Returns aggregated metrics for the tenant

## 3. Services
- `OrgSnapshotService`
- `HeadcountMetricService`
- `AttritionSignalService`
- `EngagementIndicatorService`
- `PerformanceTrendService`
- `PlanningScenarioService`
- `AiAnalysisService` - Calls LiteLLM with timeout/retry/circuit breaker
- `WorkflowExecutionService`

## 4. Frontend Application
- Next.js 15
- Testing with React Testing Library / Jest
- Pages for each domain entity:
  - `/org-snapshots`
  - `/headcount-metrics`
  - `/attrition-signals`
  - `/engagement-indicators`
  - `/performance-trends`
  - `/planning-scenarios`
- Each page lists the entities and allows creating/updating them.
- AI analysis view.

## 5. Event Contract
Emits:
- `peopleanalytics.org.signal.updated`
- `peopleanalytics.attrition.risk.detected`
- `peopleanalytics.scenario.generated`

Consumes:
- `performancenarrative.review.finalized`
- `retentionsignal.risk.detected`
- `onboardflow.milestone.completed`

## 6. Testing
- Backend unit tests using JUnit & Mockito (Service layer ≥ 80% coverage)
- Controller integration tests
- Frontend component tests

## 7. Assumptions
- For frontend testing, `vitest` with `@testing-library/react` will be used instead of Jest to match typical modern setups.
- A basic Mock LLM implementation is used for the AI endpoint to demonstrate the structure without external API calls blocking the tests.
- Global `X-Tenant-ID` header is required for all backend endpoints.
