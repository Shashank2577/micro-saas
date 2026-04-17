CREATE SCHEMA IF NOT EXISTS billingai;

CREATE TABLE billingai.subscription (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    plan_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    monthly_rate DECIMAL(19, 2) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE billingai.invoice (
    id UUID PRIMARY KEY,
    subscription_id UUID NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    tenant_id UUID NOT NULL
);
