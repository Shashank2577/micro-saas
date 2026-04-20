CREATE TABLE pipeline_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    segment VARCHAR(255),
    stage VARCHAR(255),
    count INT,
    value DECIMAL(19, 2),
    forecast_value DECIMAL(19, 2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE cac_calculations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    segment VARCHAR(255),
    channel VARCHAR(255),
    cohort_month VARCHAR(255),
    cac DECIMAL(19, 2),
    confidence_interval DECIMAL(5, 2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE ltv_models (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    segment VARCHAR(255),
    ltv DECIMAL(19, 2),
    payback_months INT,
    retention_rate DECIMAL(5, 2),
    expansion_rate DECIMAL(5, 2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE sales_velocity (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    segment VARCHAR(255),
    stage VARCHAR(255),
    median_days INT,
    p25_days INT,
    p75_days INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE forecast_accuracy (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    period VARCHAR(255),
    forecast_amount DECIMAL(19, 2),
    actual_amount DECIMAL(19, 2),
    variance_percent DECIMAL(5, 2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE gtm_gaps (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    metric_type VARCHAR(255),
    plan_value DECIMAL(19, 2),
    actual_value DECIMAL(19, 2),
    variance DECIMAL(19, 2),
    severity VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
