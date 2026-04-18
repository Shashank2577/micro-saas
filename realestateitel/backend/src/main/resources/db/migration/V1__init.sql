CREATE TABLE properties (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    property_type VARCHAR(50) NOT NULL,
    square_feet INTEGER,
    bedrooms INTEGER,
    bathrooms DECIMAL(3, 1),
    year_built INTEGER,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_properties_tenant_id ON properties(tenant_id);

CREATE TABLE comparables (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    subject_property_id UUID NOT NULL REFERENCES properties(id),
    comp_property_id UUID NOT NULL REFERENCES properties(id),
    similarity_score DECIMAL(5, 4),
    price_adjusted DECIMAL(12, 2),
    notes TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_comparables_tenant_id ON comparables(tenant_id);
CREATE INDEX idx_comparables_subject ON comparables(subject_property_id);

CREATE TABLE leases (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    property_id UUID NOT NULL REFERENCES properties(id),
    tenant_name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    monthly_rent DECIMAL(12, 2) NOT NULL,
    security_deposit DECIMAL(12, 2),
    document_url VARCHAR(255),
    ai_summary TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_leases_tenant_id ON leases(tenant_id);
CREATE INDEX idx_leases_property ON leases(property_id);

CREATE TABLE market_trends (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    month_year DATE NOT NULL,
    median_sale_price DECIMAL(12, 2) NOT NULL,
    days_on_market INTEGER,
    inventory_count INTEGER,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_market_trends_tenant_id ON market_trends(tenant_id);
CREATE INDEX idx_market_trends_zip ON market_trends(zip_code);
