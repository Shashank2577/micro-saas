CREATE TABLE claims (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    claim_number VARCHAR(100) NOT NULL,
    policy_number VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    incident_date TIMESTAMP NOT NULL,
    filed_date TIMESTAMP NOT NULL,
    ai_fraud_score DECIMAL(5, 2),
    ai_fraud_reasoning TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_claims_tenant_id ON claims(tenant_id);

CREATE TABLE policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    policy_number VARCHAR(100) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    policy_type VARCHAR(100) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    premium_amount DECIMAL(15, 2) NOT NULL,
    ai_risk_score DECIMAL(5, 2),
    ai_risk_factors TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_policies_tenant_id ON policies(tenant_id);
