CREATE SCHEMA IF NOT EXISTS contentos;

CREATE TABLE IF NOT EXISTS contentos.content_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    source_item_id UUID,
    content TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS contentos.content_calendar (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    publish_date DATE NOT NULL,
    content_id UUID,
    channel VARCHAR(100),
    owner_id VARCHAR(100),
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS contentos.content_brief (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    strategic_goal TEXT,
    target_audience TEXT,
    key_messages JSONB,
    keywords JSONB,
    generated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS contentos.content_metric (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    content_id UUID NOT NULL,
    channel VARCHAR(100) NOT NULL,
    views BIGINT DEFAULT 0,
    clicks BIGINT DEFAULT 0,
    conversions BIGINT DEFAULT 0,
    revenue_influenced DECIMAL(19, 4) DEFAULT 0,
    measured_at TIMESTAMP WITH TIME ZONE
);
