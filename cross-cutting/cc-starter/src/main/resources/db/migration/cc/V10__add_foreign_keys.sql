-- ============================================================================
-- V10: Add foreign key constraints to audit tables and indexes on FK columns
-- ============================================================================
-- Existing cc.* tables already have inline FK constraints from V1/V2/V4/V5/V6/V8.
-- Audit tables (audit.*) intentionally use SET NULL so audit records survive
-- user/tenant deletion.
-- This migration also backfills missing indexes on FK columns for query perf.
-- ============================================================================

-- ============================================================================
-- 1. FK constraints on audit tables (soft references — SET NULL on delete)
-- ============================================================================

-- audit.system_log.tenant_id -> cc.tenants(id)
DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_sys_log_tenant'
    ) THEN
        ALTER TABLE audit.system_log
            ADD CONSTRAINT fk_sys_log_tenant
            FOREIGN KEY (tenant_id) REFERENCES cc.tenants(id)
            ON DELETE SET NULL;
    END IF;
END $$;

-- audit.system_log.user_id -> cc.users(id)
DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_sys_log_user'
    ) THEN
        ALTER TABLE audit.system_log
            ADD CONSTRAINT fk_sys_log_user
            FOREIGN KEY (user_id) REFERENCES cc.users(id)
            ON DELETE SET NULL;
    END IF;
END $$;

-- audit.business_log.tenant_id -> cc.tenants(id)
DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_biz_log_tenant'
    ) THEN
        ALTER TABLE audit.business_log
            ADD CONSTRAINT fk_biz_log_tenant
            FOREIGN KEY (tenant_id) REFERENCES cc.tenants(id)
            ON DELETE SET NULL;
    END IF;
END $$;

-- audit.business_log.user_id -> cc.users(id)
DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_biz_log_user'
    ) THEN
        ALTER TABLE audit.business_log
            ADD CONSTRAINT fk_biz_log_user
            FOREIGN KEY (user_id) REFERENCES cc.users(id)
            ON DELETE SET NULL;
    END IF;
END $$;

-- audit.auth_events.tenant_id -> cc.tenants(id)
DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_auth_events_tenant'
    ) THEN
        ALTER TABLE audit.auth_events
            ADD CONSTRAINT fk_auth_events_tenant
            FOREIGN KEY (tenant_id) REFERENCES cc.tenants(id)
            ON DELETE SET NULL;
    END IF;
END $$;

-- audit.auth_events.user_id -> cc.users(id)
DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_auth_events_user'
    ) THEN
        ALTER TABLE audit.auth_events
            ADD CONSTRAINT fk_auth_events_user
            FOREIGN KEY (user_id) REFERENCES cc.users(id)
            ON DELETE SET NULL;
    END IF;
END $$;

-- ============================================================================
-- 2. Missing indexes on FK columns (for join performance)
-- ============================================================================

-- cc.roles.tenant_id — no standalone index exists
CREATE INDEX IF NOT EXISTS idx_roles_tenant ON cc.roles(tenant_id);

-- cc.user_roles.role_id — only composite idx_user_roles_tenant(tenant_id, user_id) exists
CREATE INDEX IF NOT EXISTS idx_user_roles_role ON cc.user_roles(role_id);

-- cc.resource_acl.user_id — only composite idx_acl_resource(tenant_id, resource_type, resource_id) exists
CREATE INDEX IF NOT EXISTS idx_acl_user ON cc.resource_acl(user_id);

-- cc.webhook_deliveries.endpoint_id — no index exists
CREATE INDEX IF NOT EXISTS idx_webhook_deliveries_endpoint ON cc.webhook_deliveries(endpoint_id);

-- cc.notifications.tenant_id — no index exists
CREATE INDEX IF NOT EXISTS idx_notifications_tenant ON cc.notifications(tenant_id);

-- cc.embeddings.tenant_id — no index exists
CREATE INDEX IF NOT EXISTS idx_embeddings_tenant ON cc.embeddings(tenant_id);
