CREATE SCHEMA IF NOT EXISTS invoiceprocessor;

CREATE TABLE invoiceprocessor.invoices (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    invoice_number VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    due_date DATE,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    received_at TIMESTAMP NOT NULL
);

CREATE TABLE invoiceprocessor.invoice_line_items (
    id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL REFERENCES invoiceprocessor.invoices(id),
    description TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(19, 2) NOT NULL,
    total NUMERIC(19, 2) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE invoiceprocessor.purchase_orders (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    approved_by VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE invoiceprocessor.invoice_matches (
    id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL REFERENCES invoiceprocessor.invoices(id),
    po_id UUID NOT NULL REFERENCES invoiceprocessor.purchase_orders(id),
    match_status VARCHAR(50) NOT NULL,
    discrepancy_description TEXT,
    tenant_id UUID NOT NULL
);
