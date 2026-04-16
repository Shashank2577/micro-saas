CREATE SCHEMA IF NOT EXISTS dataprivacyai;

CREATE TABLE dataprivacyai.data_flows (
    id UUID PRIMARY KEY,
    source_system VARCHAR(255) NOT NULL,
    destination_system VARCHAR(255) NOT NULL,
    data_category VARCHAR(50) NOT NULL,
    transfer_mechanism VARCHAR(255),
    legal_basis VARCHAR(50),
    tenant_id UUID NOT NULL
);

CREATE TABLE dataprivacyai.data_subject_requests (
    id UUID PRIMARY KEY,
    request_type VARCHAR(50) NOT NULL,
    subject_email VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    received_at TIMESTAMP NOT NULL,
    due_date DATE NOT NULL,
    completed_at TIMESTAMP,
    tenant_id UUID NOT NULL
);

CREATE TABLE dataprivacyai.privacy_risks (
    id UUID PRIMARY KEY,
    data_flow_id UUID NOT NULL,
    risk_type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    FOREIGN KEY (data_flow_id) REFERENCES dataprivacyai.data_flows(id)
);
