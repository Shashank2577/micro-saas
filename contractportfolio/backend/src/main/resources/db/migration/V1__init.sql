CREATE SCHEMA IF NOT EXISTS contractportfolio;

CREATE TABLE contractportfolio.contract_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    counterparty VARCHAR(255) NOT NULL,
    contract_type VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    auto_renews BOOLEAN NOT NULL DEFAULT false,
    renewal_notice_days INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE contractportfolio.extracted_terms (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    contract_id UUID NOT NULL REFERENCES contractportfolio.contract_records(id) ON DELETE CASCADE,
    term_type VARCHAR(50) NOT NULL,
    value TEXT NOT NULL,
    confidence_score DOUBLE PRECISION NOT NULL,
    source_text TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE contractportfolio.renewal_alerts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    contract_id UUID NOT NULL REFERENCES contractportfolio.contract_records(id) ON DELETE CASCADE,
    alert_date DATE NOT NULL,
    days_until_renewal INTEGER NOT NULL,
    acknowledged_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_contract_records_tenant_id ON contractportfolio.contract_records(tenant_id);
CREATE INDEX idx_extracted_terms_tenant_id_contract_id ON contractportfolio.extracted_terms(tenant_id, contract_id);
CREATE INDEX idx_renewal_alerts_tenant_id_contract_id ON contractportfolio.renewal_alerts(tenant_id, contract_id);
