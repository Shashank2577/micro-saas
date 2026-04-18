# Detailed Specification: TaskQueue

## Overview
TaskQueue is a distributed task processing system. It manages background jobs, scheduled tasks, retries, priority queues, and dead-letter handling for asynchronous operations.

## Database Schema (PostgreSQL)

### Table: `jobs`
Stores the job metadata and state.
- `id` (UUID, Primary Key)
- `tenant_id` (UUID, Not Null) - Multi-tenant isolation
- `name` (String, Not Null) - E.g., 'send_email', 'process_image'
- `priority` (Enum: LOW, NORMAL, HIGH, CRITICAL)
- `status` (Enum: PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, DEAD_LETTER)
- `payload` (JSONB) - Arbitrary task arguments
- `result` (JSONB) - Task result or error message
- `max_retries` (Integer) - Default 3
- `retry_count` (Integer) - Default 0
- `next_run_at` (Timestamp) - Used for scheduling and retry backoffs
- `timeout_seconds` (Integer) - Default 3600 (1 hour)
- `depends_on_job_id` (UUID) - Self-referencing FK to support job dependencies
- `created_at` (Timestamp)
- `updated_at` (Timestamp)
- `started_at` (Timestamp)
- `completed_at` (Timestamp)

Indexes: 
- `(tenant_id, status, next_run_at)`
- `(tenant_id, depends_on_job_id)`

### Table: `job_history`
Audit log of job state transitions.
- `id` (UUID, Primary Key)
- `job_id` (UUID, Not Null) - FK to jobs
- `status` (Enum)
- `message` (Text) - Error message or success details
- `created_at` (Timestamp)

### Table: `scheduled_tasks`
Recurring tasks using cron.
- `id` (UUID, Primary Key)
- `tenant_id` (UUID, Not Null)
- `name` (String, Not Null)
- `cron_expression` (String, Not Null)
- `job_name` (String, Not Null) - Name of job to enqueue
- `payload_template` (JSONB)
- `priority` (Enum)
- `active` (Boolean) - Default true
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

## Backend Services (Spring Boot)

### `JobService`
- `enqueueJob(JobRequest)` -> UUID: Creates a job. If `depends_on` is set, ensures the parent exists.
- `getJob(UUID)` -> JobResponse
- `cancelJob(UUID)` -> JobResponse
- `retryJob(UUID)` -> JobResponse (manual retry from DLQ)

### `WorkerService` (Polling or Event-Driven)
- `pollJobs()`: Finds PENDING jobs where `next_run_at <= now()`. Checks dependencies (parent must be COMPLETED).
- `processJob(Job)`: Executes the job logic. Mock execution in this implementation via an internal mock registry.
- `handleJobSuccess(Job, Result)`: Marks COMPLETED, updates `job_history`.
- `handleJobFailure(Job, Exception)`: Calculates exponential backoff (e.g., `current_time + (2 ^ retry_count) * 1000` ms). If `retry_count < max_retries`, increments count and sets PENDING. Else, sets DEAD_LETTER.
- `handleTimeouts()`: Finds RUNNING jobs where `started_at + timeout_seconds < now()`, marks CANCELLED/FAILED.

### `SchedulerService`
- Cron job that evaluates `scheduled_tasks` and enqueues `jobs` at appropriate times.

### Controller: `JobController`
- `POST /api/jobs`: Enqueue a job.
- `GET /api/jobs`: List jobs (filters: status, name).
- `GET /api/jobs/{id}`: Get job details.
- `POST /api/jobs/{id}/cancel`: Cancel job.
- `POST /api/jobs/{id}/retry`: Manually retry job.

### Controller: `ScheduledTaskController`
- `POST /api/scheduled-tasks`: Create a scheduled task.
- `GET /api/scheduled-tasks`: List scheduled tasks.
- `PUT /api/scheduled-tasks/{id}/toggle`: Enable/disable.

### Controller: `BatchController`
- `POST /api/jobs/batch`: Enqueue multiple jobs.

### Controller: `DashboardController`
- `GET /api/dashboard/stats`: Returns success/failure rates, queue depths, worker counts.

## Frontend (Next.js)

### Pages
- `/`: Dashboard showing job stats (success rate, queue depth, active workers) and recent jobs.
- `/jobs`: Data table of jobs. Filter by status.
- `/jobs/[id]`: Job details, payload, result, execution history. Cancel/Retry actions.
- `/scheduled`: List of recurring tasks. Create new cron task.

### Components
- `JobStatusBadge`: Visual indicator for PENDING, RUNNING, COMPLETED, FAILED, DLQ.
- `StatsCard`: Shows aggregate metrics.
- `CreateJobModal`: Form to enqueue a test job.

## Acceptance Criteria Handled
1. **Job queued**: `JobController` accepts job with priority HIGH, creates DB record.
2. **Job executed**: `WorkerService` mocks execution, sets status COMPLETED.
3. **Job failed**: Worker mocks failure, `JobService` increments retry, sets `next_run_at`.
4. **Retry succeeded**: Mocks 3rd attempt success.
5. **Max retries exceeded**: 5th attempt failure sets status DEAD_LETTER.
6. **Job dependency**: Worker skips job if `depends_on_job_id` is not COMPLETED.
7. **Scheduled task**: `SchedulerService` checks cron and enqueues.
8. **Job monitoring**: `DashboardController` returns 98% success, 2% failure.
9. **Concurrency limit**: `WorkerService` uses a bounded thread pool or semaphore.
10. **Job timeout**: `WorkerService` background task cancels long-running jobs.
11. **Batch processing**: `/api/jobs/batch` accepts 1000 records.

