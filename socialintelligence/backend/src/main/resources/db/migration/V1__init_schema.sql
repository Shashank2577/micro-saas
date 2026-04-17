CREATE TABLE social_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    platform VARCHAR(50) NOT NULL,
    handle VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    follower_count INTEGER,
    tracked_since DATE
);

CREATE TABLE brand_mentions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES social_profiles(id),
    platform VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    author_handle VARCHAR(255),
    sentiment VARCHAR(50),
    reach INTEGER,
    detected_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE trend_signals (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    topic TEXT NOT NULL,
    platform VARCHAR(50),
    signal_strength DECIMAL,
    peak_predicted_at TIMESTAMP WITH TIME ZONE,
    detected_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50)
);

CREATE TABLE post_drafts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    profile_id UUID NOT NULL REFERENCES social_profiles(id),
    content TEXT NOT NULL,
    platform VARCHAR(50) NOT NULL,
    optimal_post_time TIMESTAMP WITH TIME ZONE,
    predicted_engagement DECIMAL,
    status VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE competitor_activities (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    competitor_handle VARCHAR(255) NOT NULL,
    platform VARCHAR(50) NOT NULL,
    post_content TEXT,
    engagement INTEGER,
    observed_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_social_profiles_tenant_id ON social_profiles(tenant_id);
CREATE INDEX idx_brand_mentions_tenant_id ON brand_mentions(tenant_id);
CREATE INDEX idx_brand_mentions_detected_at ON brand_mentions(detected_at);
CREATE INDEX idx_trend_signals_tenant_id ON trend_signals(tenant_id);
CREATE INDEX idx_post_drafts_tenant_id ON post_drafts(tenant_id);
CREATE INDEX idx_competitor_activities_tenant_id ON competitor_activities(tenant_id);
