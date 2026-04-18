# InvestTracker Detailed Specification

## Overview
InvestTracker is a comprehensive investment portfolio tracking platform with multi-brokerage support, real-time sync, asset allocation tracking, performance analytics, and AI-powered risk assessment.

## Database Schema (PostgreSQL)
```sql
CREATE TABLE portfolios (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    target_allocation JSONB, -- { "stocks": 60, "bonds": 40 }
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE brokerage_accounts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    portfolio_id UUID REFERENCES portfolios(id),
    brokerage_name VARCHAR(50) NOT NULL, -- Alpaca, IB, Fidelity, Coinbase, Manual
    account_number VARCHAR(100),
    oauth_token TEXT,
    sync_status VARCHAR(20) DEFAULT 'PENDING',
    last_synced_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE assets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    ticker_symbol VARCHAR(20) NOT NULL,
    name VARCHAR(255),
    asset_class VARCHAR(50) NOT NULL, -- STOCKS, BONDS, CRYPTO, COMMODITIES, REAL_ESTATE
    current_price DECIMAL(19, 4),
    last_updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE holdings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    account_id UUID REFERENCES brokerage_accounts(id),
    asset_id UUID REFERENCES assets(id),
    quantity DECIMAL(19, 8) NOT NULL,
    average_cost_basis DECIMAL(19, 4) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tax_lots (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    holding_id UUID REFERENCES holdings(id),
    purchase_date DATE NOT NULL,
    quantity DECIMAL(19, 8) NOT NULL,
    purchase_price DECIMAL(19, 4) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    account_id UUID REFERENCES brokerage_accounts(id),
    asset_id UUID REFERENCES assets(id),
    type VARCHAR(20) NOT NULL, -- BUY, SELL, DIVIDEND, DEPOSIT, WITHDRAWAL
    quantity DECIMAL(19, 8),
    price DECIMAL(19, 4),
    total_amount DECIMAL(19, 4) NOT NULL,
    transaction_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE watchlists (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE watchlist_items (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    watchlist_id UUID REFERENCES watchlists(id),
    asset_id UUID REFERENCES assets(id),
    target_price DECIMAL(19, 4),
    alert_triggered BOOLEAN DEFAULT FALSE
);
```

## REST API Specification

### Portfolios
- `GET /api/v1/portfolios` -> `List<PortfolioDto>`
- `POST /api/v1/portfolios` (body: CreatePortfolioRequest) -> `PortfolioDto`
- `GET /api/v1/portfolios/{id}` -> `PortfolioDto`
- `GET /api/v1/portfolios/{id}/analytics` -> `PortfolioAnalyticsDto` (Total value, YTD return, Allocation, Benchmark comparison)
- `GET /api/v1/portfolios/{id}/holdings` -> `List<HoldingDto>`

### Brokerage Accounts
- `POST /api/v1/portfolios/{id}/accounts` -> `AccountDto`
- `POST /api/v1/accounts/{id}/sync` -> Trigger async sync via PGMQ

### Transactions & Tax Lots
- `GET /api/v1/portfolios/{id}/transactions` -> `Page<TransactionDto>`
- `POST /api/v1/transactions` -> `TransactionDto` (for manual entries)
- `GET /api/v1/holdings/{id}/tax-lots` -> `List<TaxLotDto>`

### AI & Risk Assessment
- `GET /api/v1/portfolios/{id}/ai-insights` -> `AiInsightDto` (Rebalancing recommendations, Risk score, Volatility metrics using LiteLLM)

### Watchlists
- `GET /api/v1/watchlists` -> `List<WatchlistDto>`
- `POST /api/v1/watchlists/{id}/items` -> `WatchlistItemDto`

## Frontend React Components
- `DashboardLayout`: Sidebar, TopNav with Portfolio Switcher
- `PortfolioSummaryCards`: Total Value, YTD Return, Risk Score
- `AssetAllocationChart`: Pie chart using Recharts / Chart.js
- `PerformanceChart`: Line chart comparing portfolio to S&P 500
- `HoldingsTable`: List of holdings with quantity, price, return
- `TaxLotViewer`: Modal/table to view lots for tax loss harvesting
- `AiRecommendationsPanel`: Displays LiteLLM generated advice
- `ConnectBrokerageModal`: OAuth flow mock for Alpaca/IB

## AI Integration Points
- When `GET /api/v1/portfolios/{id}/ai-insights` is called, the backend compiles current holdings and target allocations into a prompt.
- Sends to LiteLLM to generate JSON output containing: `riskScore` (1-100), `volatilityAssessment` (Low/Med/High), `rebalanceRecommendations` (List of assets to buy/sell).

## Testing Scenarios
- Portfolio creation and manual holding addition.
- Syncing an account triggers a PGMQ event.
- Cost basis calculation (FIFO) across tax lots.
- YTD performance vs S&P 500.
- Rebalancing threshold triggers alert when >5% drift.
