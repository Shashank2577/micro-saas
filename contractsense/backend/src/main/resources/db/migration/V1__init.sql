CREATE SCHEMA IF NOT EXISTS contractsense;

CREATE TABLE contractsense.contracts (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    counterparty VARCHAR(255) NOT NULL,
    contract_type VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,
    file_path VARCHAR(255),
    tenant_id UUID NOT NULL
);

CREATE TABLE contractsense.contract_clauses (
    id UUID PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES contractsense.contracts(id) ON DELETE CASCADE,
    clause_type VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    risk_level VARCHAR(50) NOT NULL,
    deviation_from_standard BOOLEAN NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE contractsense.risk_assessments (
    id UUID PRIMARY KEY,
    contract_id UUID NOT NULL UNIQUE REFERENCES contractsense.contracts(id) ON DELETE CASCADE,
    overall_risk_score INT NOT NULL,
    flag_count INT NOT NULL,
    missing_provisions_count INT NOT NULL,
    recommendations TEXT,
    tenant_id UUID NOT NULL
);
