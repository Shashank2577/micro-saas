-- W4: Align webhook_deliveries.attempt_count default to 0 (matching Java entity default)
-- The original V6 migration set DEFAULT 1 for "attempt" (renamed to attempt_count in V11),
-- but the Java entity initializes attemptCount = 0,
-- and the dispatch code explicitly sets attemptCount = 0 on new deliveries.
ALTER TABLE cc.webhook_deliveries ALTER COLUMN attempt_count SET DEFAULT 0;
