CREATE TABLE api_projects (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE api_versions (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    project_id UUID NOT NULL REFERENCES api_projects(id),
    version_string VARCHAR(50) NOT NULL,
    openapi_schema TEXT,
    status VARCHAR(50) NOT NULL,
    release_notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE api_endpoints (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    version_id UUID NOT NULL REFERENCES api_versions(id),
    method VARCHAR(20) NOT NULL,
    path VARCHAR(255) NOT NULL,
    operation_id VARCHAR(255),
    summary TEXT,
    description TEXT,
    is_deprecated BOOLEAN DEFAULT FALSE,
    migration_guide TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE api_keys (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    project_id UUID NOT NULL REFERENCES api_projects(id),
    developer_id VARCHAR(255) NOT NULL,
    key_hash VARCHAR(255) NOT NULL,
    prefix VARCHAR(20) NOT NULL,
    scopes TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP
);

CREATE TABLE api_analytics (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    project_id UUID NOT NULL REFERENCES api_projects(id),
    endpoint_id UUID REFERENCES api_endpoints(id),
    path VARCHAR(255) NOT NULL,
    method VARCHAR(20) NOT NULL,
    status_code INTEGER NOT NULL,
    latency_ms INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL
);
