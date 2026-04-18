# IntegrationMesh Detailed Spec

## Overview
IntegrationMesh is an AI-powered integration platform designed for AI-native SaaS apps. It manages pre-built connectors, AI-suggested field mappings, integration configuration, and synchronization health monitoring.

## Database Schema

```sql
CREATE TABLE connectors (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    config JSONB,
    status VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE integrations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    source_connector_id UUID REFERENCES connectors(id),
    target_connector_id UUID REFERENCES connectors(id),
    status VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE field_mappings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    integration_id UUID REFERENCES integrations(id),
    source_field VARCHAR(255),
    target_field VARCHAR(255),
    transform_logic TEXT,
    is_ai_suggested BOOLEAN DEFAULT false,
    confidence_score DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sync_history (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    integration_id UUID REFERENCES integrations(id),
    status VARCHAR(50),
    records_processed INTEGER,
    records_failed INTEGER,
    error_message TEXT,
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE
);
```

## Backend Services

- `ConnectorService`: Manages connectors configuration and connection tests.
- `IntegrationService`: Manages the overall logic connecting two systems.
- `FieldMappingService`: Works with LiteLLM to suggest transformations and mappings based on sample data or schema hints.
- `SyncMonitorService`: Handles sync history updates, records processed, and reports sync status.

## API Endpoints

### Connectors
- `GET /api/v1/connectors`: List connectors.
- `POST /api/v1/connectors`: Create a new connector.
- `GET /api/v1/connectors/{id}`: Get connector details.

### Integrations
- `GET /api/v1/integrations`: List integrations.
- `POST /api/v1/integrations`: Create a new integration.
- `GET /api/v1/integrations/{id}`: Get integration details.
- `PUT /api/v1/integrations/{id}/status`: Update status (ACTIVE, PAUSED, ERROR).

### Mappings
- `GET /api/v1/integrations/{id}/mappings`: Get field mappings for integration.
- `POST /api/v1/integrations/{id}/mappings`: Save field mappings.
- `POST /api/v1/integrations/{id}/mappings/suggest`: Trigger LiteLLM client to fetch AI-suggested field mappings.

### History
- `GET /api/v1/integrations/{id}/history`: Fetch sync history.

## Frontend Components

- `Dashboard`: Lists all active integrations and global health.
- `IntegrationDetails`: View specific integration status, sync history list, and field mappings overview.
- `ConnectorForm`: Reusable form component to configure different types of connectors.
- `MappingConfigurator`: UI to review and edit field mappings. Emphasizes visual indication for `isAiSuggested` mappings with `confidenceScore`.

## Assumptions
- Uses multi-tenancy based on `X-Tenant-ID`.
- Uses LiteLLM client setup within the generic cross-cutting `cc.ai` configuration.
- Simplified schema extraction format for AI field mapping suggestions.
