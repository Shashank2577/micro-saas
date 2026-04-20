CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    type VARCHAR(50),
    is_fixed BOOLEAN
);

CREATE TABLE IF NOT EXISTS accounts (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    plaid_item_id VARCHAR(255),
    plaid_account_id VARCHAR(255),
    name VARCHAR(255),
    mask VARCHAR(4),
    type VARCHAR(50),
    subtype VARCHAR(50),
    balance_current DECIMAL(19,4),
    balance_available DECIMAL(19,4),
    iso_currency_code VARCHAR(10),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    account_id UUID REFERENCES accounts(id),
    plaid_transaction_id VARCHAR(255),
    amount DECIMAL(19,4),
    date DATE,
    name VARCHAR(255),
    merchant_name VARCHAR(255),
    category_id UUID REFERENCES categories(id),
    pending BOOLEAN,
    is_recurring BOOLEAN,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS budgets (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    category_id UUID REFERENCES categories(id),
    amount DECIMAL(19,4),
    month DATE,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS recurring_charges (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    merchant_name VARCHAR(255),
    amount DECIMAL(19,4),
    frequency VARCHAR(50),
    category_id UUID REFERENCES categories(id),
    is_active BOOLEAN
);

CREATE TABLE IF NOT EXISTS cash_flow_snapshots (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    month DATE,
    total_income DECIMAL(19,4),
    total_expenses DECIMAL(19,4),
    savings_rate DECIMAL(19,4),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS spending_patterns (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    category_id UUID REFERENCES categories(id),
    trend_percentage DECIMAL(19,4),
    analysis_period VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS recommendations (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    description TEXT,
    potential_savings DECIMAL(19,4),
    is_implemented BOOLEAN,
    created_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_accounts_tenant_id ON accounts(tenant_id);
CREATE INDEX IF NOT EXISTS idx_transactions_tenant_id ON transactions(tenant_id);
CREATE INDEX IF NOT EXISTS idx_categories_tenant_id ON categories(tenant_id);
CREATE INDEX IF NOT EXISTS idx_budgets_tenant_id ON budgets(tenant_id);
CREATE INDEX IF NOT EXISTS idx_recurring_charges_tenant_id ON recurring_charges(tenant_id);
CREATE INDEX IF NOT EXISTS idx_cash_flow_snapshots_tenant_id ON cash_flow_snapshots(tenant_id);
CREATE INDEX IF NOT EXISTS idx_spending_patterns_tenant_id ON spending_patterns(tenant_id);
CREATE INDEX IF NOT EXISTS idx_recommendations_tenant_id ON recommendations(tenant_id);
