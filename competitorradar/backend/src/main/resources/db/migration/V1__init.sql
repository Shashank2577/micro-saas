CREATE TABLE competitors (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    industry VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_competitors_tenant ON competitors(tenant_id);

CREATE TABLE product_changes (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    url TEXT,
    detected_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL
);
CREATE INDEX idx_product_changes_tenant ON product_changes(tenant_id);
CREATE INDEX idx_product_changes_competitor ON product_changes(competitor_id);

CREATE TABLE pricing_changes (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    old_price DECIMAL(10, 2),
    new_price DECIMAL(10, 2),
    plan_name VARCHAR(255),
    detected_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_pricing_changes_tenant ON pricing_changes(tenant_id);

CREATE TABLE hiring_signals (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    role_title VARCHAR(255) NOT NULL,
    department VARCHAR(255),
    location VARCHAR(255),
    source VARCHAR(255),
    posted_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_hiring_signals_tenant ON hiring_signals(tenant_id);

CREATE TABLE customer_reviews (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    platform VARCHAR(255) NOT NULL,
    rating INTEGER,
    text TEXT,
    category VARCHAR(255),
    sentiment_score DECIMAL(3, 2),
    posted_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_customer_reviews_tenant ON customer_reviews(tenant_id);

CREATE TABLE social_mentions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    platform VARCHAR(255) NOT NULL,
    text TEXT,
    url TEXT,
    sentiment_score DECIMAL(3, 2),
    posted_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_social_mentions_tenant ON social_mentions(tenant_id);

CREATE TABLE press_mentions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    source VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    url TEXT,
    sentiment_score DECIMAL(3, 2),
    published_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_press_mentions_tenant ON press_mentions(tenant_id);

CREATE TABLE battlecards (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    content TEXT,
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_battlecards_tenant ON battlecards(tenant_id);

CREATE TABLE win_loss_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    outcome VARCHAR(50) NOT NULL,
    reason TEXT,
    value DECIMAL(12, 2),
    date TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_win_loss_records_tenant ON win_loss_records(tenant_id);

CREATE TABLE features (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255)
);
CREATE INDEX idx_features_tenant ON features(tenant_id);

CREATE TABLE competitor_features (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_id UUID NOT NULL REFERENCES competitors(id) ON DELETE CASCADE,
    feature_id UUID NOT NULL REFERENCES features(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL
);
CREATE INDEX idx_competitor_features_tenant ON competitor_features(tenant_id);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    competitor_id UUID REFERENCES competitors(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_alerts_tenant ON alerts(tenant_id);
