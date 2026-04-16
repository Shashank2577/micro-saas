CREATE SCHEMA IF NOT EXISTS securitypulse;

CREATE TABLE IF NOT EXISTS findings (
    id UUID PRIMARY KEY,
    pr_url VARCHAR(255) NOT NULL,
    tool VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS policies (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rule VARCHAR(255) NOT NULL,
    action VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);
