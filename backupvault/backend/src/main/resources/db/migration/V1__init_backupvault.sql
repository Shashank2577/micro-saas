CREATE TABLE backup_policies (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    schedule_cron VARCHAR(100) NOT NULL,
    retention_days INT NOT NULL,
    encryption_key_id VARCHAR(255),
    cross_region_replication BOOLEAN DEFAULT FALSE,
    target_region VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE backup_executions (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    policy_id UUID REFERENCES backup_policies(id),
    status VARCHAR(50) NOT NULL,
    backup_type VARCHAR(50) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    original_size_bytes BIGINT,
    compressed_size_bytes BIGINT,
    storage_path VARCHAR(500),
    replication_status VARCHAR(50),
    encryption_verified BOOLEAN DEFAULT FALSE,
    integrity_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE backup_restores (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    execution_id UUID REFERENCES backup_executions(id),
    status VARCHAR(50) NOT NULL,
    target_environment VARCHAR(100) NOT NULL,
    point_in_time TIMESTAMP,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE disaster_recovery_drills (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    rto_minutes INT,
    successful BOOLEAN,
    report_notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
