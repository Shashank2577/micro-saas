CREATE TABLE refinance_opportunities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    debt_id UUID NOT NULL REFERENCES debts(id) ON DELETE CASCADE,
    new_apr DECIMAL(5, 4) NOT NULL,
    potential_savings DECIMAL(19, 4) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_refinance_ops_tenant_id ON refinance_opportunities(tenant_id);
CREATE INDEX idx_refinance_ops_debt_id ON refinance_opportunities(debt_id);

CREATE TABLE credit_score_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    score INTEGER NOT NULL,
    recorded_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_credit_score_hist_tenant_id ON credit_score_history(tenant_id);

CREATE TABLE payment_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    debt_id UUID NOT NULL REFERENCES debts(id) ON DELETE CASCADE,
    amount_paid DECIMAL(19, 4) NOT NULL,
    payment_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_payment_hist_tenant_id ON payment_history(tenant_id);
CREATE INDEX idx_payment_hist_debt_id ON payment_history(debt_id);

CREATE TABLE creditor_contacts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    debt_id UUID NOT NULL REFERENCES debts(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    email VARCHAR(255),
    website VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_creditor_contacts_tenant_id ON creditor_contacts(tenant_id);
CREATE INDEX idx_creditor_contacts_debt_id ON creditor_contacts(debt_id);
