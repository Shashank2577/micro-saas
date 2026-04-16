CREATE TABLE expense (
    id UUID PRIMARY KEY,
    vendor VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    submitted_by VARCHAR(255) NOT NULL,
    expense_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE policy_violation (
    id UUID PRIMARY KEY,
    expense_id UUID NOT NULL,
    violation_type VARCHAR(50) NOT NULL,
    description TEXT,
    severity VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    CONSTRAINT fk_policy_violation_expense FOREIGN KEY (expense_id) REFERENCES expense (id) ON DELETE CASCADE
);

CREATE TABLE subscription (
    id UUID PRIMARY KEY,
    vendor VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    billing_cycle VARCHAR(50) NOT NULL,
    category VARCHAR(255),
    is_redundant BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);
