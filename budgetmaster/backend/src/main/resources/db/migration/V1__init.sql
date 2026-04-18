CREATE TABLE bm_budgets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    monthly_income DECIMAL(19,2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    rolling_mode BOOLEAN NOT NULL DEFAULT FALSE,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE bm_categories (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    budget_id UUID NOT NULL REFERENCES bm_budgets(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    allocated_amount DECIMAL(19,2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    carryover BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE bm_transactions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    budget_id UUID NOT NULL REFERENCES bm_budgets(id) ON DELETE CASCADE,
    category_id UUID REFERENCES bm_categories(id) ON DELETE SET NULL,
    amount DECIMAL(19,2) NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE bm_alerts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    category_id UUID NOT NULL REFERENCES bm_categories(id) ON DELETE CASCADE,
    threshold_percent DECIMAL(5,2),
    threshold_amount DECIMAL(19,2),
    triggered BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE bm_targets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    target_amount DECIMAL(19,2) NOT NULL,
    current_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
    deadline DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE bm_family_members (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    individual_allowance DECIMAL(19,2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE bm_irregular_expenses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    due_date DATE NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
