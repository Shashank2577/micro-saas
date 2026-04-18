CREATE TABLE users (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    department VARCHAR(255),
    role VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE access_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    resource_accessed VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL,
    ip_address VARCHAR(255),
    access_time TIMESTAMP NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE anomalies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    access_log_id UUID REFERENCES access_logs(id),
    anomaly_type VARCHAR(255) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    detected_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE access_reviews (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    reviewer_id UUID NOT NULL REFERENCES users(id),
    target_user_id UUID NOT NULL REFERENCES users(id),
    review_period_start TIMESTAMP NOT NULL,
    review_period_end TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    ai_recommendation TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE privileges (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    resource_name VARCHAR(255) NOT NULL,
    permission_level VARCHAR(255) NOT NULL,
    last_used_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
