-- V13__platform_bootstrap.sql
-- Platform bootstrapping: default tenant, FlagOverride.createdAt fix

-- 1. Default system tenant (well-known UUID for reference)
INSERT INTO cc.tenants (id, name, slug, status, settings, created_at, updated_at)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Default',
    'default',
    'active',
    '{}',
    NOW(),
    NOW()
) ON CONFLICT (slug) DO NOTHING;

-- 2. Add missing created_at to feature_flag_overrides (entity has @PrePersist but column was missing)
ALTER TABLE cc.feature_flag_overrides
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ NOT NULL DEFAULT NOW();
