CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE platform_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    platform_name VARCHAR(50) NOT NULL,
    platform_account_id VARCHAR(255) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    access_token TEXT NOT NULL,
    refresh_token TEXT,
    token_expires_at TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(tenant_id, platform_name, platform_account_id)
);

CREATE TABLE engagement_metrics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    metric_date DATE NOT NULL,
    followers_count BIGINT DEFAULT 0,
    following_count BIGINT DEFAULT 0,
    likes_count BIGINT DEFAULT 0,
    comments_count BIGINT DEFAULT 0,
    shares_count BIGINT DEFAULT 0,
    views_count BIGINT DEFAULT 0,
    engagement_rate DECIMAL(5,2) DEFAULT 0.0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(account_id, metric_date)
);

CREATE TABLE audience_demographics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    demographic_type VARCHAR(50) NOT NULL,
    demographic_value VARCHAR(255) NOT NULL,
    percentage DECIMAL(5,2) NOT NULL,
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE content_analyses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    content_id VARCHAR(255) NOT NULL,
    content_url TEXT,
    content_type VARCHAR(50),
    published_at TIMESTAMP WITH TIME ZONE,
    likes BIGINT DEFAULT 0,
    comments BIGINT DEFAULT 0,
    shares BIGINT DEFAULT 0,
    views BIGINT DEFAULT 0,
    ai_pattern_analysis TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(account_id, content_id)
);

CREATE TABLE growth_recommendations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    platform_name VARCHAR(50) NOT NULL,
    recommendation_type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    projected_impact TEXT,
    status VARCHAR(50) DEFAULT 'NEW',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_platform_accounts_tenant ON platform_accounts(tenant_id);
CREATE INDEX idx_engagement_metrics_account_date ON engagement_metrics(account_id, metric_date);
CREATE INDEX idx_content_analyses_account ON content_analyses(account_id);
CREATE INDEX idx_growth_recommendations_tenant ON growth_recommendations(tenant_id);
