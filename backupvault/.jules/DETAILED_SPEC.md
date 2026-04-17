# BackupVault Detailed Specification

## Overview
BackupVault is a multi-tenant micro-SaaS application for automated backups, point-in-time recovery, cross-region replication, and disaster recovery.

## Database Schema

```sql
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
    backup_type VARCHAR(50) NOT NULL, -- FULL, INCREMENTAL, DIFFERENTIAL
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
```

## REST Endpoints

### Backup Policies
- `POST /api/policies`: Create a new policy
  - Request: `{ name, scheduleCron, retentionDays, encryptionKeyId, crossRegionReplication, targetRegion }`
  - Response: Policy object
- `GET /api/policies`: List all policies for tenant
- `GET /api/policies/{id}`: Get policy details

### Backup Executions
- `POST /api/executions`: Trigger a manual backup
  - Request: `{ policyId, backupType }`
  - Response: Execution object
- `GET /api/executions`: List recent executions
- `GET /api/executions/{id}`: Get execution status

### Restores
- `POST /api/restores`: Start a restore operation
  - Request: `{ executionId, targetEnvironment, pointInTime }`
  - Response: Restore object
- `GET /api/restores`: List recent restores

### Disaster Recovery
- `POST /api/dr-drills`: Start a DR drill
  - Request: `{ targetRegion }`
  - Response: Drill object

### AI Suggestions
- `GET /api/ai/optimize-schedule`: Get AI suggestions for backup schedule optimization.

## Service Layer
- `BackupPolicyService`: Manages policies.
- `BackupExecutionService`: Coordinates backup jobs (simulated processing for the scope of this project).
- `RestoreService`: Coordinates restores (simulated processing).
- `DisasterRecoveryService`: Simulates region failure and verifies RTO/RPO.
- `BackupAIAnalyzerService`: Uses LiteLLM to suggest backup optimizations based on sizes and times.

## Frontend
- `DashboardPage`: Overview of recent backups, RTO/RPO status, storage usage (React Query).
- `PoliciesPage`: List and create backup policies.
- `ExecutionsPage`: View backup history, manually trigger backups.
- `RestoresPage`: UI for initiating restores, including PITR selection.

## Edge Cases
- Overlapping backup schedules -> Queue via Redis (simulated with status PENDING).
- Failed encryption -> Reject backup execution immediately.
- Corrupted backup -> Mark `integrity_verified=false` and alert.

