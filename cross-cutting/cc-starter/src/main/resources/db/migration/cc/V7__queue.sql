-- Try pgmq if extension is available, otherwise use SKIP LOCKED pattern
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_available_extensions WHERE name = 'pgmq') THEN
        CREATE EXTENSION IF NOT EXISTS pgmq;
        PERFORM pgmq.create('cc_jobs');
        PERFORM pgmq.create('cc_notifications');
        PERFORM pgmq.create('cc_webhooks');
        PERFORM pgmq.create('cc_exports');
    ELSE
        -- Fallback: SKIP LOCKED pattern
        CREATE TABLE IF NOT EXISTS cc.job_queue (
            id BIGSERIAL PRIMARY KEY,
            queue_name VARCHAR(100) NOT NULL,
            payload JSONB NOT NULL,
            status VARCHAR(20) NOT NULL DEFAULT 'pending',
            attempts INT NOT NULL DEFAULT 0,
            max_attempts INT NOT NULL DEFAULT 3,
            scheduled_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
            started_at TIMESTAMPTZ,
            completed_at TIMESTAMPTZ,
            error TEXT,
            created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
        );
        CREATE INDEX IF NOT EXISTS idx_job_queue_poll
            ON cc.job_queue(queue_name, status, scheduled_at)
            WHERE status IN ('pending', 'retry');
    END IF;
END $$;
