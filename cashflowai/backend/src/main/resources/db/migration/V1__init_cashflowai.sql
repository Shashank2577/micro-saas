CREATE TABLE cash_position (
    id UUID PRIMARY KEY,
    date DATE NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    source VARCHAR(50) NOT NULL,
    confidence_interval DOUBLE PRECISION,
    tenant_id UUID NOT NULL
);

CREATE TABLE cashflow_forecast (
    id UUID PRIMARY KEY,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    inflow_forecast DECIMAL(19, 2) NOT NULL,
    outflow_forecast DECIMAL(19, 2) NOT NULL,
    net_forecast DECIMAL(19, 2) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE scenario (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    assumptions TEXT,
    projected_runway_days INT,
    tenant_id UUID NOT NULL
);

CREATE TABLE cashflow_alert (
    id UUID PRIMARY KEY,
    alert_type VARCHAR(50) NOT NULL,
    projected_date DATE,
    severity VARCHAR(50) NOT NULL,
    recommended_action TEXT,
    tenant_id UUID NOT NULL
);
