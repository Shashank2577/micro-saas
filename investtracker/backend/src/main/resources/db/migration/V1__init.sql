CREATE TABLE portfolios (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    target_allocation JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE brokerage_accounts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    portfolio_id UUID REFERENCES portfolios(id),
    brokerage_name VARCHAR(50) NOT NULL,
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
    asset_class VARCHAR(50) NOT NULL,
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
    type VARCHAR(20) NOT NULL,
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

CREATE INDEX idx_portfolios_tenant_id ON portfolios(tenant_id);
CREATE INDEX idx_brokerage_accounts_tenant_id ON brokerage_accounts(tenant_id);
CREATE INDEX idx_assets_tenant_id ON assets(tenant_id);
CREATE INDEX idx_holdings_tenant_id ON holdings(tenant_id);
CREATE INDEX idx_tax_lots_tenant_id ON tax_lots(tenant_id);
CREATE INDEX idx_transactions_tenant_id ON transactions(tenant_id);
CREATE INDEX idx_watchlists_tenant_id ON watchlists(tenant_id);
CREATE INDEX idx_watchlist_items_tenant_id ON watchlist_items(tenant_id);
