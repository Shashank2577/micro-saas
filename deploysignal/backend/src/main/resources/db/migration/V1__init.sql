CREATE TABLE deployment (
    id UUID PRIMARY KEY,
    tenant_id UUID,
    version VARCHAR(255),
    environment VARCHAR(255),
    outcome VARCHAR(255)
);
