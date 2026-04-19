# CashflowAnalyzer Detailed Specification

## 1) Product Intent
- **App:** `cashflowanalyzer`
- **Domain:** Finance Operations
- **Outcome:** Historical cashflow diagnostics, forecasting, and anomaly explanations
- **Primary actors:** Finance analyst, CFO, Controller

## 2) Domain Model (Database Schema)

All entities must support multi-tenancy (`tenant_id`) and standard audit fields (`created_at`, `updated_at`).

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE cashflow_periods (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cash_movements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE trend_signals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE forecast_runs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE anomaly_flags (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE narrative_insights (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cashflow_periods_tenant ON cashflow_periods(tenant_id);
CREATE INDEX idx_cash_movements_tenant ON cash_movements(tenant_id);
CREATE INDEX idx_trend_signals_tenant ON trend_signals(tenant_id);
CREATE INDEX idx_forecast_runs_tenant ON forecast_runs(tenant_id);
CREATE INDEX idx_anomaly_flags_tenant ON anomaly_flags(tenant_id);
CREATE INDEX idx_narrative_insights_tenant ON narrative_insights(tenant_id);
```

## 3) REST API Contract

### CashflowPeriods API (`/api/v1/cashflow/cashflow-periods`)
- `GET /`: List CashflowPeriods (response: `List<CashflowPeriod>`). Filters: None. Pagination support.
- `POST /`: Create CashflowPeriod. Request: `{ "name": "...", "status": "..." }`. Response: `CashflowPeriod`.
- `GET /{id}`: Get CashflowPeriod by ID.
- `PATCH /{id}`: Update CashflowPeriod. Request: `{ "name": "...", "status": "..." }`.
- `POST /{id}/validate`: Validate CashflowPeriod. Response: `{ "valid": true/false }`.

### CashMovements API (`/api/v1/cashflow/cash-movements`)
- `GET /`: List CashMovements.
- `POST /`: Create CashMovement.
- `GET /{id}`: Get CashMovement by ID.
- `PATCH /{id}`: Update CashMovement.
- `POST /{id}/validate`: Validate CashMovement.

### TrendSignals API (`/api/v1/cashflow/trend-signals`)
- `GET /`: List TrendSignals.
- `POST /`: Create TrendSignal.
- `GET /{id}`: Get TrendSignal by ID.
- `PATCH /{id}`: Update TrendSignal.
- `POST /{id}/validate`: Validate TrendSignal.

### ForecastRuns API (`/api/v1/cashflow/forecast-runs`)
- `GET /`: List ForecastRuns.
- `POST /`: Create ForecastRun.
- `GET /{id}`: Get ForecastRun by ID.
- `PATCH /{id}`: Update ForecastRun.
- `POST /{id}/validate`: Validate ForecastRun.

### AnomalyFlags API (`/api/v1/cashflow/anomaly-flags`)
- `GET /`: List AnomalyFlags.
- `POST /`: Create AnomalyFlag.
- `GET /{id}`: Get AnomalyFlag by ID.
- `PATCH /{id}`: Update AnomalyFlag.
- `POST /{id}/validate`: Validate AnomalyFlag.

### AI & Workflow APIs
- `POST /api/v1/cashflow/ai/analyze`: Run AI Analysis. Request: `{ "context": "..." }`. Response: `{ "result": "..." }`.
- `POST /api/v1/cashflow/ai/recommendations`: Get AI Recommendations. Request: `{ "context": "..." }`. Response: `{ "recommendations": [...] }`.
- `POST /api/v1/cashflow/workflows/execute`: Trigger async workflow.
- `GET /api/v1/cashflow/health/contracts`: Health check and contracts definition.
- `GET /api/v1/cashflow/metrics/summary`: High-level metrics summary.

## 4) Service Methods Contract
The following backend services must implement these core operations (CRUD + validate/simulate):
1. **IngestionService**: `create()`, `update()`, `list()`, `getById()`, `delete()`, `validate()`, `simulate()`
2. **AnalysisService**: `create()`, `update()`, `list()`, `getById()`, `delete()`, `validate()`, `simulate()`
3. **ForecastingService**: `create()`, `update()`, `list()`, `getById()`, `delete()`, `validate()`, `simulate()`
4. **AnomaliesService**: `create()`, `update()`, `list()`, `getById()`, `delete()`, `validate()`, `simulate()`
5. **InsightsService**: `create()`, `update()`, `list()`, `getById()`, `delete()`, `validate()`, `simulate()`
6. **ReportingService**: `create()`, `update()`, `list()`, `getById()`, `delete()`, `validate()`, `simulate()`

*Note:* Entities like `CashflowPeriod` will typically be handled by `IngestionService` and `ReportingService`, `CashMovement` by `IngestionService` and `AnalysisService`, etc. For simplicity, we can align domains directly with these service layers:
- `CashflowPeriod` -> `IngestionService`
- `CashMovement` -> `AnalysisService`
- `TrendSignal` -> `InsightsService`
- `ForecastRun` -> `ForecastingService`
- `AnomalyFlag` -> `AnomaliesService`
- `NarrativeInsight` -> `ReportingService`

## 5) AI Integration (LiteLLM)
- Wrap LiteLLM inside an `AiAnalysisService`.
- Implement circuit breaker and retry mechanism (Exponential backoff).
- All AI operations are synchronously executed but traced (audit logged).

## 6) Event Emitters & Consumers
- **Emits:**
  - `cashflowanalyzer.forecast.generated`
  - `cashflowanalyzer.anomaly.detected`
  - `cashflowanalyzer.insight.published`
- **Consumes:**
  - `invoiceprocessor.invoice.approved`
  - `budgetpilot.reforecast.completed`
  - `financenarrator.summary.requested`

## 7) Frontend Pages (Next.js App Router)
- `/ingestion`: Manage CashflowPeriods (Ingestion Service).
- `/analysis`: Manage CashMovements (Analysis Service).
- `/forecasting`: Manage ForecastRuns (Forecasting Service).
- `/anomalies`: Manage AnomalyFlags (Anomalies Service).
- `/insights`: Manage TrendSignals (Insights Service).
- `/reporting`: Manage NarrativeInsights (Reporting Service).

Pages should include React tables/lists, detail views, and edit forms. Add vitest and Testing Library tests for each page.

## 8) Error Handling
- Invalid ID format: 400 Bad Request
- Not found: 404 Not Found
- Tenant violation: 403 Forbidden (Cross-tenant access prevented by `TenantContext.require()`).
- Validation failures: 400 Bad Request with details.

## 9) Acceptance Criteria
- Tenant isolation enforced on all reads/writes.
- Service unit tests with >=80% coverage.
- UI component tests complete.
- Integration contracts align exactly.
