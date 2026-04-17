-- V11__fix_schema_mismatches.sql
-- Fixes entity-migration mismatches discovered during TaskForge integration testing.
-- See docs/plans/entity-migration-disconnects.md for full audit.

------------------------------------------------------------
-- 1. Add missing columns
------------------------------------------------------------
ALTER TABLE cc.users
    ADD COLUMN IF NOT EXISTS last_login_at TIMESTAMPTZ;

ALTER TABLE cc.feature_flags
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW();

------------------------------------------------------------
-- 2. Fix audit inet → VARCHAR(45) for JPA String mapping
------------------------------------------------------------
ALTER TABLE audit.system_log
    ALTER COLUMN ip_address TYPE VARCHAR(45) USING ip_address::VARCHAR;

ALTER TABLE audit.business_log
    ALTER COLUMN ip_address TYPE VARCHAR(45) USING ip_address::VARCHAR;

ALTER TABLE audit.auth_events
    ALTER COLUMN ip_address TYPE VARCHAR(45) USING ip_address::VARCHAR;

------------------------------------------------------------
-- 3. Fix webhook_endpoints: TEXT[] → TEXT, status → active
------------------------------------------------------------
ALTER TABLE cc.webhook_endpoints
    RENAME COLUMN events TO event_types;

ALTER TABLE cc.webhook_endpoints
    ALTER COLUMN event_types TYPE TEXT USING array_to_string(event_types, ',');

ALTER TABLE cc.webhook_endpoints
    ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE;

-- Migrate status values to boolean
UPDATE cc.webhook_endpoints SET active = (status = 'active') WHERE status IS NOT NULL;

ALTER TABLE cc.webhook_endpoints
    DROP COLUMN IF EXISTS status;

ALTER TABLE cc.webhook_endpoints
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();

------------------------------------------------------------
-- 4. Fix webhook_deliveries: rename attempt, payload type
------------------------------------------------------------
ALTER TABLE cc.webhook_deliveries
    RENAME COLUMN attempt TO attempt_count;

ALTER TABLE cc.webhook_deliveries
    ALTER COLUMN payload TYPE TEXT USING payload::TEXT;
