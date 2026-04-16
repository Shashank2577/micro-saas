-- D7: The CHECK constraint on feature_flag_overrides prevents global overrides
-- (where both tenant_id and user_id are NULL). The FeatureFlagService supports
-- global overrides via setOverride(key, null, null, enabled), so we need to
-- drop this constraint.
--
-- The override hierarchy is: user > tenant > global > default.
-- A global override has tenant_id IS NULL AND user_id IS NULL, which the old
-- CHECK (tenant_id IS NOT NULL OR user_id IS NOT NULL) rejected.

ALTER TABLE cc.feature_flag_overrides
    DROP CONSTRAINT IF EXISTS feature_flag_overrides_check;

-- Add a created_at column for consistency with the JPA entity
ALTER TABLE cc.feature_flag_overrides
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ NOT NULL DEFAULT NOW();
