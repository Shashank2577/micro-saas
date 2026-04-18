CREATE TABLE filing_obligations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE jurisdiction_schedules (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE submission_packets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE filing_deadlines (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE validation_checks (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE submission_receipts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
