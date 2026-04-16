CREATE SCHEMA IF NOT EXISTS policyforge;

CREATE TABLE policyforge.policies (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    department VARCHAR(255),
    version INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    owner VARCHAR(255),
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE policyforge.policy_versions (
    id UUID PRIMARY KEY,
    policy_id UUID NOT NULL,
    version INT NOT NULL,
    content TEXT NOT NULL,
    changed_by VARCHAR(255),
    changed_at TIMESTAMP NOT NULL,
    change_summary TEXT,
    tenant_id UUID NOT NULL,
    CONSTRAINT fk_policy_versions_policy_id FOREIGN KEY (policy_id) REFERENCES policyforge.policies(id)
);

CREATE TABLE policyforge.policy_acknowledgments (
    id UUID PRIMARY KEY,
    policy_id UUID NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    acknowledged_at TIMESTAMP NOT NULL,
    tenant_id UUID NOT NULL,
    CONSTRAINT fk_policy_ack_policy_id FOREIGN KEY (policy_id) REFERENCES policyforge.policies(id)
);
