CREATE TABLE purchase_requests (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    requested_by VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    category VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE approval_steps (
    id UUID PRIMARY KEY,
    request_id UUID NOT NULL REFERENCES purchase_requests(id),
    approver_role VARCHAR(255) NOT NULL,
    approver_email VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    decided_at TIMESTAMP WITH TIME ZONE,
    comments TEXT,
    tenant_id UUID NOT NULL
);

CREATE TABLE purchase_orders (
    id UUID PRIMARY KEY,
    request_id UUID NOT NULL REFERENCES purchase_requests(id),
    vendor_name VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    issued_at TIMESTAMP WITH TIME ZONE,
    expected_delivery DATE,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE budget_checks (
    id UUID PRIMARY KEY,
    request_id UUID NOT NULL REFERENCES purchase_requests(id),
    category VARCHAR(255) NOT NULL,
    requested_amount DECIMAL(19, 2) NOT NULL,
    available_budget DECIMAL(19, 2) NOT NULL,
    passed BOOLEAN NOT NULL,
    tenant_id UUID NOT NULL
);
