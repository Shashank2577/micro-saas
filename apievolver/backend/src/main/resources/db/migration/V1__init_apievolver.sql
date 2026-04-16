CREATE TABLE api_spec (
    id BIGSERIAL PRIMARY KEY,
    service_id VARCHAR(255) NOT NULL,
    version VARCHAR(255) NOT NULL,
    spec_type VARCHAR(50) NOT NULL,
    spec_content TEXT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL
);

CREATE TABLE api_change (
    id BIGSERIAL PRIMARY KEY,
    spec_id BIGINT NOT NULL REFERENCES api_spec(id),
    old_version VARCHAR(255) NOT NULL,
    new_version VARCHAR(255) NOT NULL,
    changes JSONB,
    breaking_changes JSONB
);

CREATE TABLE api_dependency (
    id BIGSERIAL PRIMARY KEY,
    consumer_service_id VARCHAR(255) NOT NULL,
    provider_service_id VARCHAR(255) NOT NULL,
    used_endpoints JSONB,
    sensitive_to_breaking_changes BOOLEAN NOT NULL DEFAULT true
);
