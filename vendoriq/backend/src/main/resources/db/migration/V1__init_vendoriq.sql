CREATE TABLE vendors (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    contact_email VARCHAR(255),
    contract_end_date DATE,
    monthly_spend DECIMAL(19, 2),
    sla_uptime DOUBLE PRECISION,
    health_score INT,
    status VARCHAR(50),
    tenant_id UUID NOT NULL
);

CREATE TABLE sla_violations (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    violation_type VARCHAR(50),
    description TEXT,
    occurred_at TIMESTAMP,
    severity VARCHAR(50),
    status VARCHAR(50),
    tenant_id UUID NOT NULL
);

CREATE TABLE renewal_alerts (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    alert_date DATE,
    days_until_renewal INT,
    market_rate_benchmark DECIMAL(19, 2),
    current_rate DECIMAL(19, 2),
    tenant_id UUID NOT NULL
);
