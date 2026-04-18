CREATE TABLE subscription_plans (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_price DECIMAL(10, 2) NOT NULL,
    billing_period VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_plan_tenant ON subscription_plans(tenant_id);

CREATE TABLE pricing_models (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    plan_id UUID NOT NULL REFERENCES subscription_plans(id),
    metric_name VARCHAR(255) NOT NULL,
    model_type VARCHAR(50) NOT NULL,
    unit_price DECIMAL(10, 4) NOT NULL,
    included_units INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_pm_tenant ON pricing_models(tenant_id);
CREATE INDEX idx_pm_plan ON pricing_models(plan_id);

CREATE TABLE subscriptions (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    plan_id UUID NOT NULL REFERENCES subscription_plans(id),
    status VARCHAR(50) NOT NULL,
    current_period_start TIMESTAMP NOT NULL,
    current_period_end TIMESTAMP NOT NULL,
    payment_method_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_sub_tenant ON subscriptions(tenant_id);
CREATE INDEX idx_sub_plan ON subscriptions(plan_id);

CREATE TABLE meter_events (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    subscription_id UUID NOT NULL REFERENCES subscriptions(id),
    metric_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    event_timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_me_tenant ON meter_events(tenant_id);
CREATE INDEX idx_me_sub ON meter_events(subscription_id);
CREATE INDEX idx_me_ts ON meter_events(event_timestamp);

CREATE TABLE invoices (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    subscription_id UUID NOT NULL REFERENCES subscriptions(id),
    amount_due DECIMAL(10, 2) NOT NULL,
    amount_paid DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    due_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_inv_tenant ON invoices(tenant_id);
CREATE INDEX idx_inv_sub ON invoices(subscription_id);

CREATE TABLE invoice_line_items (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    invoice_id UUID NOT NULL REFERENCES invoices(id),
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_ili_tenant ON invoice_line_items(tenant_id);
CREATE INDEX idx_ili_inv ON invoice_line_items(invoice_id);

CREATE TABLE payments (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    invoice_id UUID NOT NULL REFERENCES invoices(id),
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    processor_transaction_id VARCHAR(255),
    error_message TEXT,
    retry_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_pay_tenant ON payments(tenant_id);
CREATE INDEX idx_pay_inv ON payments(invoice_id);

CREATE TABLE refunds (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    payment_id UUID NOT NULL REFERENCES payments(id),
    amount DECIMAL(10, 2) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_ref_tenant ON refunds(tenant_id);
CREATE INDEX idx_ref_pay ON refunds(payment_id);
