# CashflowAI Detailed Implementation Specification

## 1. Overview
This is the detailed specification for the `cashflowai` application, aiming to provide near-term cash position forecasting and shortfall mitigation automation.

## 2. Domain Models

### CashPosition
```sql
CREATE TABLE cash_positions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    as_of DATE NOT NULL,
    available_cash NUMERIC(14,2) NOT NULL,
    restricted_cash NUMERIC(14,2) NOT NULL
);
CREATE INDEX idx_cash_positions_tenant_id ON cash_positions(tenant_id);
```

### LiquidityForecast
```sql
CREATE TABLE liquidity_forecasts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_liquidity_forecasts_tenant_id ON liquidity_forecasts(tenant_id);
```

### ShortfallAlert
```sql
CREATE TABLE shortfall_alerts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_shortfall_alerts_tenant_id ON shortfall_alerts(tenant_id);
```

### MitigationOption
```sql
CREATE TABLE mitigation_options (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_mitigation_options_tenant_id ON mitigation_options(tenant_id);
```

### ScenarioRun
```sql
CREATE TABLE scenario_runs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_scenario_runs_tenant_id ON scenario_runs(tenant_id);
```

### FundingEvent
```sql
CREATE TABLE funding_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_funding_events_tenant_id ON funding_events(tenant_id);
```

## 3. API Contract

Base Path: `/api/v1/cash-position`

### CashPositions
- `GET /cash-positions` -> List positions
- `POST /cash-positions` -> Create position
- `GET /cash-positions/{id}` -> Get position
- `PATCH /cash-positions/{id}` -> Update position
- `POST /cash-positions/{id}/validate` -> Validate position

### LiquidityForecasts
- `GET /liquidity-forecasts` -> List forecasts
- `POST /liquidity-forecasts` -> Create forecast
- `GET /liquidity-forecasts/{id}` -> Get forecast
- `PATCH /liquidity-forecasts/{id}` -> Update forecast
- `POST /liquidity-forecasts/{id}/validate` -> Validate forecast

### ShortfallAlerts
- `GET /shortfall-alerts` -> List alerts
- `POST /shortfall-alerts` -> Create alert
- `GET /shortfall-alerts/{id}` -> Get alert
- `PATCH /shortfall-alerts/{id}` -> Update alert
- `POST /shortfall-alerts/{id}/validate` -> Validate alert

### MitigationOptions
- `GET /mitigation-options` -> List mitigations
- `POST /mitigation-options` -> Create mitigation
- `GET /mitigation-options/{id}` -> Get mitigation
- `PATCH /mitigation-options/{id}` -> Update mitigation
- `POST /mitigation-options/{id}/validate` -> Validate mitigation

### ScenarioRuns
- `GET /scenario-runs` -> List runs
- `POST /scenario-runs` -> Create run
- `GET /scenario-runs/{id}` -> Get run
- `PATCH /scenario-runs/{id}` -> Update run
- `POST /scenario-runs/{id}/validate` -> Validate run

### FundingEvents
- `GET /funding-events` -> List events
- `POST /funding-events` -> Create event
- `GET /funding-events/{id}` -> Get event
- `PATCH /funding-events/{id}` -> Update event
- `POST /funding-events/{id}/validate` -> Validate event

### Extra Endpoints
- `POST /ai/analyze`
- `POST /ai/recommendations`
- `POST /workflows/execute`
- `GET /health/contracts`
- `GET /metrics/summary`

## 4. Services

All standard service modules (positionsService, forecastsService, shortfallsService, mitigationsService, scenariosService, eventsService) need to implement:
- `create(DTO)`
- `update(UUID, DTO)`
- `list()`
- `getById(UUID)`
- `delete(UUID)`
- `validate(UUID)`
- `simulate(UUID)`

## 5. Event & Integration Contract
**Emits:**
- `cashflowai.position.updated`
- `cashflowai.shortfall.detected`
- `cashflowai.mitigation.recommended`

**Consumes:**
- `invoiceprocessor.invoice.approved`
- `budgetpilot.variance.alerted`
- `debtnavigator.plan.approved`

## 6. Acceptance Criteria
- All 6 module slices are implemented and wired from UI to backend.
- All endpoints return tenant-scoped data and reject cross-tenant access.
- Write endpoints publish domain events.
- OpenAPI docs.
- Backend tests.
- Frontend tests.
- Async workflows (simulated).
- LiteLLM integration.
- Env var contracts validated.
- Integration manifest correctly tracks events.
