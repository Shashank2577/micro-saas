CREATE TABLE cache_policies (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    app_name VARCHAR(255) NOT NULL,
    namespace VARCHAR(255) NOT NULL,
    ttl_seconds BIGINT NOT NULL,
    strategy VARCHAR(50) NOT NULL,
    compression_enabled BOOLEAN DEFAULT false,
    stale_while_revalidate BOOLEAN DEFAULT false,
    stale_ttl_seconds BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_cache_policies_tenant ON cache_policies(tenant_id);

CREATE TABLE cache_analytics (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    namespace VARCHAR(255) NOT NULL,
    hit_count BIGINT DEFAULT 0,
    miss_count BIGINT DEFAULT 0,
    total_size_bytes BIGINT DEFAULT 0,
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_cache_analytics_tenant ON cache_analytics(tenant_id);
