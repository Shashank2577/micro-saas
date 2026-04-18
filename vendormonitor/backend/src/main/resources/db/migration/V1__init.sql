CREATE TABLE vendors (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    status VARCHAR(50) NOT NULL, -- ACTIVE, INACTIVE, IN_REVIEW
    contact_email VARCHAR(255),
    website VARCHAR(255),
    sla_description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_vendors_tenant ON vendors(tenant_id);

CREATE TABLE contracts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE,
    value_amount DECIMAL(15,2),
    value_currency VARCHAR(3) DEFAULT 'USD',
    sla_response_time_minutes INTEGER,
    sla_uptime_percentage DECIMAL(5,2),
    auto_renew BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_contracts_tenant ON contracts(tenant_id);
CREATE INDEX idx_contracts_vendor ON contracts(vendor_id);

CREATE TABLE performance_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    record_type VARCHAR(50) NOT NULL, -- TICKET, UPTIME, INVOICE, MANUAL_REVIEW
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL,
    metric_value DECIMAL(10,2),
    metric_unit VARCHAR(50),
    is_sla_breach BOOLEAN DEFAULT FALSE,
    description TEXT,
    evidence_url VARCHAR(1024),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_perf_records_tenant ON performance_records(tenant_id);
CREATE INDEX idx_perf_records_vendor ON performance_records(vendor_id);
CREATE INDEX idx_perf_records_date ON performance_records(recorded_at);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    severity VARCHAR(50) NOT NULL, -- INFO, WARNING, CRITICAL
    status VARCHAR(50) NOT NULL, -- OPEN, ACKNOWLEDGED, RESOLVED
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    resolved_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_alerts_tenant ON alerts(tenant_id);
CREATE INDEX idx_alerts_vendor ON alerts(vendor_id);
CREATE INDEX idx_alerts_status ON alerts(status);

CREATE TABLE renewal_summaries (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    contract_id UUID REFERENCES contracts(id),
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    overall_score INTEGER NOT NULL, -- 0-100
    ai_summary TEXT NOT NULL,
    recommendation VARCHAR(50) NOT NULL, -- RENEW, RENEGOTIATE, CANCEL
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_renewal_summaries_tenant ON renewal_summaries(tenant_id);
CREATE INDEX idx_renewal_summaries_vendor ON renewal_summaries(vendor_id);
