CREATE SCHEMA IF NOT EXISTS taxdataorganizer;

CREATE TABLE taxdataorganizer.tax_year (
    id UUID PRIMARY KEY,
    year INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE taxdataorganizer.transaction (
    id UUID PRIMARY KEY,
    tax_year_id UUID NOT NULL REFERENCES taxdataorganizer.tax_year(id),
    description VARCHAR(255),
    amount DECIMAL(19, 2) NOT NULL,
    transaction_date DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    is_deductible BOOLEAN NOT NULL DEFAULT FALSE,
    notes TEXT,
    tenant_id UUID NOT NULL
);

CREATE TABLE taxdataorganizer.tax_document (
    id UUID PRIMARY KEY,
    tax_year_id UUID NOT NULL REFERENCES taxdataorganizer.tax_year(id),
    document_type VARCHAR(50) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE taxdataorganizer.tax_summary (
    id UUID PRIMARY KEY,
    tax_year_id UUID NOT NULL REFERENCES taxdataorganizer.tax_year(id),
    total_revenue DECIMAL(19, 2) NOT NULL,
    total_deductions DECIMAL(19, 2) NOT NULL,
    net_taxable_income DECIMAL(19, 2) NOT NULL,
    transaction_count INT NOT NULL,
    document_count INT NOT NULL,
    generated_at TIMESTAMP NOT NULL,
    tenant_id UUID NOT NULL
);
