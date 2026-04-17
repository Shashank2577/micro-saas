CREATE TABLE debts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL,
    apr DECIMAL(5, 4) NOT NULL,
    minimum_payment DECIMAL(19, 4) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_debts_tenant_id ON debts(tenant_id);

CREATE TABLE debt_payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    debt_id UUID NOT NULL REFERENCES debts(id) ON DELETE CASCADE,
    amount DECIMAL(19, 4) NOT NULL,
    payment_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_debt_payments_tenant_id ON debt_payments(tenant_id);
CREATE INDEX idx_debt_payments_debt_id ON debt_payments(debt_id);

CREATE TABLE payoff_strategies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    strategy_type VARCHAR(50) NOT NULL,
    total_interest DECIMAL(19, 4),
    payoff_date DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_payoff_strategies_tenant_id ON payoff_strategies(tenant_id);
