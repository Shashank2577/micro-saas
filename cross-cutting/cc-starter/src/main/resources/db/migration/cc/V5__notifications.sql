-- TODO: This table is currently unused. Notification channels are defined as
-- Java beans (InAppChannel, EmailChannel) implementing the NotificationChannel interface.
-- This table was intended for dynamic channel registration but is not referenced by any
-- entity, repository, or service. Consider removing in a future migration or implementing
-- dynamic channel lookup.
CREATE TABLE cc.notification_channels (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE cc.notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID REFERENCES cc.tenants(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES cc.users(id) ON DELETE CASCADE,
    channel VARCHAR(50) NOT NULL,
    title VARCHAR(500) NOT NULL,
    body TEXT,
    data JSONB NOT NULL DEFAULT '{}',
    read_at TIMESTAMPTZ,
    sent_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE cc.notification_preferences (
    user_id UUID NOT NULL REFERENCES cc.users(id) ON DELETE CASCADE,
    channel VARCHAR(50) NOT NULL,
    category VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (user_id, channel, category)
);

CREATE INDEX idx_notifications_user ON cc.notifications(user_id, read_at NULLS FIRST, created_at DESC);
