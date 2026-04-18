CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE market_segment (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    keywords TEXT[],
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_market_segment_tenant_id ON market_segment(tenant_id);

CREATE TABLE information_source (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    source_type VARCHAR(50) NOT NULL,
    url VARCHAR(500),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_information_source_tenant_id ON information_source(tenant_id);

CREATE TABLE market_signal (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    source_id UUID REFERENCES information_source(id),
    published_at TIMESTAMP WITH TIME ZONE,
    signal_strength INTEGER CHECK (signal_strength BETWEEN 1 AND 10),
    sentiment VARCHAR(50),
    strategic_implications TEXT,
    embedding vector(1536),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_market_signal_tenant_id ON market_signal(tenant_id);
CREATE INDEX idx_market_signal_published_at ON market_signal(published_at);
-- Optional vector index for similarity search could be added here if performance needed.

CREATE TABLE market_pattern (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    pattern_type VARCHAR(50) NOT NULL,
    detected_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_market_pattern_tenant_id ON market_pattern(tenant_id);

CREATE TABLE market_pattern_signals (
    pattern_id UUID REFERENCES market_pattern(id),
    signal_id UUID REFERENCES market_signal(id),
    PRIMARY KEY (pattern_id, signal_id)
);

CREATE TABLE market_brief (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    summary TEXT,
    content TEXT,
    generated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_market_brief_tenant_id ON market_brief(tenant_id);
