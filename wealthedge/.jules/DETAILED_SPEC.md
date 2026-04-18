# WealthEdge - Detailed Specification

## 1. Overview
WealthEdge is a high-net-worth wealth management platform. Features include net worth tracking (traditional + alternative assets), real estate portfolio, alternative investments, tax planning, charitable giving strategies, family wealth coordination, trust/estate planning, succession planning, business interests valuation, risk concentration analysis, benchmarking, and advisor coordination workspace.

## 2. Database Schema (PostgreSQL via Flyway)
Entities include tenant isolation (`tenant_id`), auditing, and necessary constraints.

```sql
CREATE TABLE asset (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL, -- e.g., TRADITIONAL, REAL_ESTATE, ALTERNATIVE, BUSINESS
    name VARCHAR(255) NOT NULL,
    current_value NUMERIC(19, 2) NOT NULL,
    purchase_value NUMERIC(19, 2),
    purchase_date DATE,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT NOT NULL
);

CREATE INDEX idx_asset_tenant ON asset(tenant_id);

CREATE TABLE portfolio (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    total_value NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT NOT NULL
);

CREATE INDEX idx_portfolio_tenant ON portfolio(tenant_id);

CREATE TABLE asset_portfolio (
    asset_id UUID NOT NULL REFERENCES asset(id),
    portfolio_id UUID NOT NULL REFERENCES portfolio(id),
    PRIMARY KEY (asset_id, portfolio_id)
);
```

## 3. Backend (Spring Boot)

### 3.1 Entities
- `Asset`: Maps to `asset`. Enum for `type`.
- `Portfolio`: Maps to `portfolio`. OneToMany with Assets.

### 3.2 Repositories
- `AssetRepository`: `findAllByTenantId()`, `findByTenantIdAndId()`
- `PortfolioRepository`: `findAllByTenantId()`, `findByTenantIdAndId()`

### 3.3 Services
- `AssetService`: Add, edit, list, delete assets.
- `PortfolioService`: Summarize portfolio value.
- `AIWealthService`: Uses LiteLLM to analyze risk concentration and offer tax planning insights based on portfolio data.

### 3.4 REST Controllers
- `GET /api/assets`
- `POST /api/assets`
- `GET /api/portfolios`
- `POST /api/portfolios`
- `GET /api/insights/risk-analysis`

## 4. Frontend (Next.js)

### 4.1 Pages
- `/`: Dashboard showing overall net worth and asset breakdown chart.
- `/assets`: List and add assets (real estate, stocks, etc.).
- `/insights`: View AI-generated risk concentration and tax planning insights.

### 4.2 Components
- `AssetCard`: Visual summary of an asset.
- `NetWorthChart`: react-chartjs-2 pie chart of asset types.
- `AIInsightPanel`: Displays LiteLLM insights.

## 5. Acceptance Criteria
1. User can add traditional and alternative assets with their valuation.
2. User can view total net worth accurately summarizing all assets.
3. System properly scopes data by `tenant_id`.
4. AI Service can consume asset data and return risk insights.

