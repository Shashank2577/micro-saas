CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    industry VARCHAR(255),
    mrr DECIMAL(10, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE customer_health_scores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    customer_id UUID REFERENCES customers(id),
    composite_score DECIMAL(5,2),
    usage_score DECIMAL(5,2),
    support_score DECIMAL(5,2),
    engagement_score DECIMAL(5,2),
    nps_score DECIMAL(5,2),
    calculated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE churn_predictions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    customer_id UUID REFERENCES customers(id),
    risk_segment VARCHAR(50),
    probability_30_days DECIMAL(5,2),
    probability_60_days DECIMAL(5,2),
    probability_90_days DECIMAL(5,2),
    predicted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE intervention_playbooks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    trigger_risk_segment VARCHAR(50),
    action_type VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE interventions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    customer_id UUID REFERENCES customers(id),
    playbook_id UUID REFERENCES intervention_playbooks(id),
    status VARCHAR(50),
    offer_details TEXT,
    effectiveness_status VARCHAR(50),
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_customers_tenant ON customers(tenant_id);
CREATE INDEX idx_customer_health_scores_tenant ON customer_health_scores(tenant_id);
CREATE INDEX idx_churn_predictions_tenant ON churn_predictions(tenant_id);
CREATE INDEX idx_intervention_playbooks_tenant ON intervention_playbooks(tenant_id);
CREATE INDEX idx_interventions_tenant ON interventions(tenant_id);
