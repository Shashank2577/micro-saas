CREATE SCHEMA IF NOT EXISTS creatoranalytics;

CREATE TABLE creatoranalytics.content_channel (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    platform VARCHAR(50) NOT NULL,
    url TEXT NOT NULL,
    tracked_since DATE NOT NULL
);

CREATE TABLE creatoranalytics.content_performance (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    content_title TEXT NOT NULL,
    content_url TEXT NOT NULL,
    views BIGINT NOT NULL,
    clicks BIGINT NOT NULL,
    watch_time_minutes INT NOT NULL,
    measured_at DATE NOT NULL
);

CREATE TABLE creatoranalytics.business_outcome (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    outcome_type VARCHAR(50) NOT NULL,
    content_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    attributed_value DECIMAL NOT NULL,
    occurred_at TIMESTAMP NOT NULL
);

CREATE TABLE creatoranalytics.performance_model (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    content_type TEXT NOT NULL,
    predicted_views INT NOT NULL,
    predicted_conversions INT NOT NULL,
    confidence_score DECIMAL NOT NULL,
    modeled_at TIMESTAMP NOT NULL
);

CREATE TABLE creatoranalytics.content_insight (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    insight_type VARCHAR(50) NOT NULL,
    insight_text TEXT NOT NULL,
    evidence JSONB,
    generated_at TIMESTAMP NOT NULL
);
