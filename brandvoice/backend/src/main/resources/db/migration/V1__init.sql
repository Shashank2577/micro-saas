CREATE SCHEMA IF NOT EXISTS brandvoice;

CREATE TABLE brandvoice.brand_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    tone_attributes JSONB,
    vocabulary_approved JSONB,
    vocabulary_banned JSONB,
    style_guide TEXT,
    consistency_score DECIMAL(5, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE brandvoice.content_reviews (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    brand_profile_id UUID NOT NULL REFERENCES brandvoice.brand_profiles(id),
    content TEXT NOT NULL,
    consistency_score DECIMAL(5, 2),
    deviations JSONB,
    suggestions JSONB,
    reviewed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE brandvoice.brand_corpus_items (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    brand_profile_id UUID NOT NULL REFERENCES brandvoice.brand_profiles(id),
    title VARCHAR(255),
    content TEXT NOT NULL,
    approved BOOLEAN DEFAULT FALSE,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE brandvoice.consistency_trends (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    brand_profile_id UUID NOT NULL REFERENCES brandvoice.brand_profiles(id),
    period DATE NOT NULL,
    avg_score DECIMAL(5, 2),
    review_count INT DEFAULT 0
);
