# CompensationOS Detailed Specification

## 1. Overview
CompensationOS is a micro-SaaS platform within the ecosystem designed for compensation benchmarking, equity modeling, and budget forecasting. It enables companies to analyze their internal compensation structures against market data, ensure pay equity, and plan future compensation budgets.

## 2. Database Schema (PostgreSQL)

```sql
-- Flyway Migration V1__init.sql

CREATE TABLE market_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    role VARCHAR(255) NOT NULL,
    level VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    p25_salary DECIMAL(10, 2),
    p50_salary DECIMAL(10, 2),
    p75_salary DECIMAL(10, 2),
    p90_salary DECIMAL(10, 2),
    data_source VARCHAR(100),
    effective_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_market_data_tenant_role ON market_data(tenant_id, role);

CREATE TABLE employee_compensation (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role VARCHAR(255) NOT NULL,
    level VARCHAR(50) NOT NULL,
    department VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    base_salary DECIMAL(10, 2) NOT NULL,
    bonus_target_percent DECIMAL(5, 2),
    equity_grant_value DECIMAL(10, 2),
    currency VARCHAR(3) DEFAULT 'USD',
    gender VARCHAR(50),
    ethnicity VARCHAR(100),
    hire_date DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_employee_comp_tenant ON employee_compensation(tenant_id);

CREATE TABLE compensation_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    change_date DATE NOT NULL,
    previous_base DECIMAL(10, 2),
    new_base DECIMAL(10, 2),
    reason VARCHAR(255),
    approved_by UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comp_hist_tenant_emp ON compensation_history(tenant_id, employee_id);

CREATE TABLE equity_models (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    plan_name VARCHAR(255) NOT NULL,
    total_shares BIGINT,
    available_shares BIGINT,
    current_valuation DECIMAL(10, 2),
    vesting_schedule_months INT DEFAULT 48,
    cliff_months INT DEFAULT 12,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE compensation_cycles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    cycle_name VARCHAR(255) NOT NULL,
    start_date DATE,
    end_date DATE,
    total_budget DECIMAL(15, 2),
    status VARCHAR(50) DEFAULT 'DRAFT', -- DRAFT, ACTIVE, COMPLETED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

## 3. REST Endpoints

### Market Data API
- `GET /api/market-data`
  - Query Params: `role`, `location`
  - Returns: List of `MarketDataResponse`
- `POST /api/market-data/import`
  - Body: List of market data records or CSV upload
  - Returns: Status

### Employee Compensation API
- `GET /api/employees`
  - Query Params: `department`, `role`
  - Returns: List of `EmployeeCompResponse`
- `POST /api/employees`
  - Body: `EmployeeCompRequest`
  - Returns: `EmployeeCompResponse`
- `GET /api/employees/{id}/benchmark`
  - Returns: Employee comp vs market data comparison.

### Analysis & Dashboards API
- `GET /api/analysis/pay-equity`
  - Returns: Pay gaps aggregated by gender/ethnicity per role.
- `GET /api/analysis/budget-forecast`
  - Query Params: `months` (default 12)
  - Returns: Monthly projected compensation costs.
- `GET /api/analysis/market-trends`
  - Returns: Trends over time for specific roles.

### Equity Modeling API
- `GET /api/equity`
  - Returns: Current equity plans and pools.
- `POST /api/equity/grant-calculator`
  - Body: Shares, Vesting Start, Plan ID
  - Returns: Projected vesting schedule and value.

### Compensation Planning
- `GET /api/cycles`
- `POST /api/cycles`
- `POST /api/cycles/{id}/model-scenario`
  - Body: `IncreasePercent`, `BudgetCap`
  - Returns: Modeled salary adjustments for eligible employees.

## 4. Services

- `MarketDataService`: Handles CRUD and bulk import of market data.
- `EmployeeCompensationService`: Manages employee records and history.
- `BenchmarkingService`: Compares internal comp to market data.
- `PayEquityService`: Runs statistical analysis (using LiteLLM for insight generation) on pay gaps.
- `EquityModelingService`: Calculates vesting and valuations.
- `BudgetingService`: Forecasts future costs based on current payroll and planned increases.

## 5. React Components (Next.js)

- `DashboardPage`: Overview of comp metrics, market trend charts.
- `EmployeesPage`: Table of employees, edit comp details.
- `EmployeeBenchmarkingModal`: D3/Chart.js visual of employee comp vs P25/P50/P75/P90.
- `PayEquityPage`: Charts showing pay by demographic, highlighting gaps.
- `EquityModelingPage`: Calculator for grants.
- `CompensationCyclesPage`: Planning tools and scenarios.

## 6. AI Integration

- Use `LiteLLM` via `AiService` in `PayEquityService` and `BenchmarkingService`.
  - Provide anonymized summary stats to LLM to generate plain-English compliance reports and recommendations for addressing pay gaps.

## 7. Error Handling
- Use Spring `@ControllerAdvice` for consistent error responses (400, 404, 500).
- Wrap LiteLLM calls with circuit breakers (Resilience4j).

## 8. Acceptance Criteria
1. Can import market data.
2. Benchmarking API returns correct P-values comparison.
3. Pay equity gap is calculated accurately.
4. Multitenancy works (data isolation).
