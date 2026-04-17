-- Create all required schemas before any other migrations run.
-- Uses IF NOT EXISTS for idempotency.

CREATE SCHEMA IF NOT EXISTS cc;
CREATE SCHEMA IF NOT EXISTS audit;
CREATE SCHEMA IF NOT EXISTS tenant;
