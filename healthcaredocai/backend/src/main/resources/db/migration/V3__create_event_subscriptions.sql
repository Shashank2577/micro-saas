CREATE TABLE IF NOT EXISTS tenant.event_subscriptions (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id            UUID         NOT NULL,
    subscriber_app       VARCHAR(100) NOT NULL,
    event_type_pattern   VARCHAR(200) NOT NULL,
    callback_url         VARCHAR(500) NOT NULL,
    secret               VARCHAR(200),
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_subscription UNIQUE (tenant_id, subscriber_app, event_type_pattern)
);

CREATE INDEX idx_event_subscriptions_tenant ON tenant.event_subscriptions (tenant_id);
