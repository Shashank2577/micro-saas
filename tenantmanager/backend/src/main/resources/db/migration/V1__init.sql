CREATE TABLE customer_tenant (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    external_tenant_id UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    health_score INTEGER NOT NULL DEFAULT 100,
    churn_risk VARCHAR(50) NOT NULL DEFAULT 'LOW',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_customer_tenant_tenant_id ON customer_tenant(tenant_id);

CREATE TABLE onboarding_milestone (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    customer_tenant_id UUID NOT NULL REFERENCES customer_tenant(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_onboarding_milestone_tenant_id ON onboarding_milestone(tenant_id);
CREATE INDEX idx_onboarding_milestone_customer_tenant_id ON onboarding_milestone(customer_tenant_id);

CREATE TABLE tenant_event (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    customer_tenant_id UUID NOT NULL REFERENCES customer_tenant(id),
    event_type VARCHAR(100) NOT NULL,
    description TEXT,
    occurred_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_tenant_event_tenant_id ON tenant_event(tenant_id);
CREATE INDEX idx_tenant_event_customer_tenant_id ON tenant_event(customer_tenant_id);
