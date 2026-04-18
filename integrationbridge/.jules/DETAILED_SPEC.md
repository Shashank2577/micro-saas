# IntegrationBridge Detailed Specification

## 1. Overview
IntegrationBridge is a platform for connecting third-party services. It manages OAuth flows, API credentials, data sync jobs, error handling, and transformation rules.

## 2. Database Schema
PostgreSQL will be used. Every table must have a `tenant_id` for multitenancy.

- `integrations`
  - `id` (UUID, PK)
  - `tenant_id` (UUID)
  - `provider` (VARCHAR: STRIPE, SENDGRID, etc.)
  - `status` (VARCHAR: HEALTHY, ERROR, PENDING)
  - `auth_type` (VARCHAR: OAUTH2, BASIC_AUTH, API_KEY)
  - `created_at` (TIMESTAMP)
  - `updated_at` (TIMESTAMP)

- `api_credentials`
  - `id` (UUID, PK)
  - `integration_id` (UUID, FK)
  - `tenant_id` (UUID)
  - `encrypted_token` (VARCHAR)
  - `refresh_token` (VARCHAR)
  - `token_expiry` (TIMESTAMP)
  - `username` (VARCHAR)
  - `encrypted_password` (VARCHAR)
  - `created_at` (TIMESTAMP)

- `sync_jobs`
  - `id` (UUID, PK)
  - `integration_id` (UUID, FK)
  - `tenant_id` (UUID)
  - `schedule_cron` (VARCHAR)
  - `source_entity` (VARCHAR)
  - `target_entity` (VARCHAR)
  - `status` (VARCHAR)
  - `last_run_at` (TIMESTAMP)
  - `next_run_at` (TIMESTAMP)

- `field_mappings`
  - `id` (UUID, PK)
  - `sync_job_id` (UUID, FK)
  - `tenant_id` (UUID)
  - `source_field` (VARCHAR)
  - `target_field` (VARCHAR)
  - `transformation_rule` (VARCHAR)

- `audit_logs`
  - `id` (UUID, PK)
  - `tenant_id` (UUID)
  - `integration_id` (UUID, FK)
  - `action` (VARCHAR)
  - `status` (VARCHAR)
  - `records_processed` (INTEGER)
  - `error_message` (TEXT)
  - `created_at` (TIMESTAMP)

- `synced_records` (for local storage of synced data)
  - `id` (UUID, PK)
  - `tenant_id` (UUID)
  - `integration_id` (UUID, FK)
  - `external_id` (VARCHAR)
  - `data` (JSONB)
  - `synced_at` (TIMESTAMP)

## 3. REST API Endpoints

- `POST /api/integrations`: Create a new integration
- `GET /api/integrations`: List integrations
- `GET /api/integrations/{id}`: Get integration details
- `POST /api/integrations/{id}/oauth/authorize`: Start OAuth flow
- `POST /api/integrations/{id}/oauth/callback`: Handle OAuth callback
- `POST /api/integrations/{id}/credentials`: Add API credentials
- `POST /api/integrations/{id}/test`: Test connection
- `POST /api/integrations/{id}/sync-jobs`: Create a sync job
- `POST /api/sync-jobs/{id}/run`: Manually run a sync job
- `POST /api/webhooks/{provider}`: Handle incoming webhooks
- `GET /api/integrations/{id}/audit-logs`: Get audit logs

## 4. Services
- `IntegrationService`: CRUD for integrations.
- `CredentialService`: Encrypts/decrypts credentials, manages OAuth tokens.
- `SyncService`: Executes sync jobs, applies field mappings and transformations. Uses Quartz/Spring Task.
- `WebhookService`: Processes incoming webhooks.
- `AuditService`: Logs actions.
- `LLMService`: Uses LiteLLM to suggest field mappings.

## 5. Frontend
- Pages:
  - `/`: Dashboard showing integration health.
  - `/integrations`: List of integrations.
  - `/integrations/[id]`: Integration details, sync jobs, credentials.
- Components:
  - `IntegrationList`
  - `SyncJobForm`
  - `CredentialForm`
  - `AuditLogViewer`
  - `WebhookConfigurator`

## 6. Integrations & Third-Party
- Spring Task for scheduled jobs.
- LiteLLM integration for field mapping suggestions.
- Mock Stripe API in tests for validation.

## 7. Error Handling
- Use `@RestControllerAdvice` for global error handling.
- Sync job failures should trigger retries with exponential backoff (max 5 attempts).

