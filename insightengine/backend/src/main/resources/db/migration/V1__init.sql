CREATE TABLE metric_data (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    metric_value DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    segment VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_metric_data_tenant_metric ON metric_data(tenant_id, metric_name, timestamp);

CREATE TABLE insights (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL, -- ANOMALY, CORRELATION, TREND, SEGMENT, COMPARISON
    title VARCHAR(255) NOT NULL,
    description TEXT,
    explanation TEXT,
    recommended_action TEXT,
    impact_score DOUBLE PRECISION NOT NULL, -- 0.0 to 1.0
    confidence_score DOUBLE PRECISION NOT NULL, -- 0.0 to 1.0
    metric_names VARCHAR(255)[] NOT NULL,
    status VARCHAR(50) DEFAULT 'NEW', -- NEW, ACKNOWLEDGED, RESOLVED
    assigned_to UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_insights_tenant_score ON insights(tenant_id, impact_score DESC);

CREATE TABLE insight_comments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    insight_id UUID NOT NULL REFERENCES insights(id),
    user_id UUID NOT NULL,
    comment_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE alert_rules (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    metric_name VARCHAR(255),
    condition_type VARCHAR(50) NOT NULL, -- ANOMALY_SEVERITY, TREND_ACCELERATION, etc.
    threshold DOUBLE PRECISION NOT NULL,
    slack_channel VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE custom_discovery_rules (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    definition JSONB NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
