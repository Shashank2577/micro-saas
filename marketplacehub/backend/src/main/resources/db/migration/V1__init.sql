CREATE TABLE apps (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255),
    tags JSONB,
    screenshots JSONB,
    pricing_tiers JSONB,
    developer_id VARCHAR(255),
    version VARCHAR(50),
    status VARCHAR(50) DEFAULT 'PUBLIC',
    trending_score INT DEFAULT 0,
    total_installations INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE app_installations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    app_id UUID NOT NULL,
    workspace_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'TRIAL',
    trial_ends_at TIMESTAMP,
    installed_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_installation_app FOREIGN KEY (app_id) REFERENCES apps(id)
);

CREATE TABLE app_reviews (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    app_id UUID NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    rating DECIMAL(3,2) NOT NULL,
    review_text TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_review_app FOREIGN KEY (app_id) REFERENCES apps(id)
);

CREATE TABLE app_revenues (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    app_id UUID NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    revenue_month INT NOT NULL,
    revenue_year INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_revenue_app FOREIGN KEY (app_id) REFERENCES apps(id)
);

CREATE TABLE permission_grants (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    installation_id UUID NOT NULL,
    permission_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'GRANTED',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_grant_installation FOREIGN KEY (installation_id) REFERENCES app_installations(id)
);

CREATE INDEX idx_apps_tenant ON apps(tenant_id);
CREATE INDEX idx_app_installations_tenant ON app_installations(tenant_id);
CREATE INDEX idx_app_reviews_tenant ON app_reviews(tenant_id);
CREATE INDEX idx_app_revenues_tenant ON app_revenues(tenant_id);
CREATE INDEX idx_permission_grants_tenant ON permission_grants(tenant_id);
