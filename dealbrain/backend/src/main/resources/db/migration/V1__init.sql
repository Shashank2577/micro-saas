CREATE TABLE deals (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255),
    amount DOUBLE PRECISION,
    stage VARCHAR(100),
    expected_close_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_deals_tenant ON deals(tenant_id);

CREATE TABLE deal_activities (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    deal_id UUID NOT NULL REFERENCES deals(id),
    type VARCHAR(100),
    description TEXT,
    activity_timestamp TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_activities_tenant_deal ON deal_activities(tenant_id, deal_id);

CREATE TABLE deal_risk_signals (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    deal_id UUID NOT NULL REFERENCES deals(id),
    signal_type VARCHAR(100),
    severity VARCHAR(50),
    description TEXT,
    detected_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_signals_tenant_deal ON deal_risk_signals(tenant_id, deal_id);

CREATE TABLE deal_recommendations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    deal_id UUID NOT NULL REFERENCES deals(id),
    action VARCHAR(255),
    reason TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_recommendations_tenant_deal ON deal_recommendations(tenant_id, deal_id);

CREATE TABLE stakeholders (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    deal_id UUID NOT NULL REFERENCES deals(id),
    name VARCHAR(255),
    email VARCHAR(255),
    role VARCHAR(100),
    engagement_level VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_stakeholders_tenant_deal ON stakeholders(tenant_id, deal_id);

CREATE TABLE historical_deals (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    deal_id UUID NOT NULL,
    name VARCHAR(255),
    amount DOUBLE PRECISION,
    close_date TIMESTAMP,
    outcome VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_historical_tenant ON historical_deals(tenant_id);
