# Detailed Spec: ContextLayer

## Schema Definitions
- `CustomerContext` (customer_id, tenant_id, profile (JSONB), preferences (JSONB), attributes (JSONB), last_updated_at, updated_by_app)
- `ContextVersion` (version_id, customer_id, context_snapshot (JSONB), created_at, created_by_app, change_description)
- `InteractionHistory` (interaction_id, customer_id, app_id, interaction_type, timestamp, metadata (JSONB), outcomes)
- `CustomerPreference` (customer_id, preference_key, preference_value, source_app, valid_from, valid_until)
- `ConsentRecord` (customer_id, consent_type, granted, consented_at, consented_by_app, expiry_date)

## API Endpoints
- `GET /api/customers/{customerId}/context` -> Retrieve context.
- `PUT /api/customers/{customerId}/context` -> Merge/update context.
- `GET /api/customers/{customerId}/context/{attribute}` -> Get single attribute.
- `PATCH /api/customers/{customerId}/context/{attribute}` -> Update attribute.
- `POST /api/customers/{customerId}/context/version` -> Create snapshot.
- `GET /api/customers/{customerId}/context/versions` -> List versions.
- `POST /api/customers/{customerId}/context/rollback` -> Rollback.
- `GET /api/customers/{customerId}/preferences` -> List preferences.
- `PUT /api/customers/{customerId}/preferences/{key}` -> Update preference.
- `POST /api/customers/{customerId}/consent` -> Record consent.
- `GET /api/customers/{customerId}/audit-log` -> Get changes.
- `POST /api/customers/{customerId}/context/export` -> Export.
- `DELETE /api/customers/{customerId}/context` -> Delete context.
- `GET /api/context-sync/watch?customerId=X` -> WebSocket for sync.

## Services
- `ContextRetrievalService`: Retrieves and caches Context.
- `ContextUpdateService`: Updates and versions Context.
- `PreferenceLearningService`: Uses AI to learn preferences.
- `ContextEnrichmentService`: Derives segment, LTV.
- `PrivacyEnforcementService`: Enforces consent.
- `RealtimeSyncService`: Publishes events.

## Frontend
- Dashboard Page: Displays context overview.
- Customer View Page: Context viewer, version history, audit logs, preferences, consent.
