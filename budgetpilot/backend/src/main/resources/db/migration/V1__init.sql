CREATE TABLE budgets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    fiscal_year INT NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- DRAFT, ACTIVE, ARCHIVED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE budget_items (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    budget_id UUID NOT NULL REFERENCES budgets(id) ON DELETE CASCADE,
    category VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    allocated_amount DECIMAL(15, 2) NOT NULL,
    actual_amount DECIMAL(15, 2) DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE expenses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    budget_item_id UUID NOT NULL REFERENCES budget_items(id) ON DELETE CASCADE,
    amount DECIMAL(15, 2) NOT NULL,
    description TEXT,
    date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE variances (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    budget_item_id UUID NOT NULL REFERENCES budget_items(id) ON DELETE CASCADE,
    variance_amount DECIMAL(15, 2) NOT NULL,
    variance_percentage DECIMAL(5, 2) NOT NULL,
    explanation TEXT,
    date_calculated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_budgets_tenant ON budgets(tenant_id);
CREATE INDEX idx_budget_items_tenant_budget ON budget_items(tenant_id, budget_id);
CREATE INDEX idx_expenses_tenant_item ON expenses(tenant_id, budget_item_id);
CREATE INDEX idx_variances_tenant_item ON variances(tenant_id, budget_item_id);
