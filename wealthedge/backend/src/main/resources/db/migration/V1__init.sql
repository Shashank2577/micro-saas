CREATE TABLE asset (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
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
