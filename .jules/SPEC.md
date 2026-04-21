# ContextLayer - Unified Customer Context for AI Apps

## Overview
Build ContextLayer, a unified context management platform that shares customer knowledge across all AI applications in real-time.

## Requirements
- Unified customer context store (JSONB)
- Real-time context sync
- Context versioning
- Preference learning (AI-driven)
- Privacy controls & Consent management
- Context enrichment
- Conflict resolution

## Domain Entities
- CustomerContext
- ContextVersion
- InteractionHistory
- CustomerPreference
- ConsentRecord

## Services
- ContextRetrievalService
- ContextUpdateService
- PreferenceLearningService (uses LiteLLM/Claude)
- ContextEnrichmentService
- PrivacyEnforcementService
- RealtimeSyncService (WebSocket/pgmq)

## Endpoints
- GET /api/customers/{customerId}/context
- PUT /api/customers/{customerId}/context
- GET /api/customers/{customerId}/context/{attribute}
- PATCH /api/customers/{customerId}/context/{attribute}
- POST /api/customers/{customerId}/context/version
- GET /api/customers/{customerId}/context/versions
- POST /api/customers/{customerId}/context/rollback
- GET /api/customers/{customerId}/preferences
- PUT /api/customers/{customerId}/preferences/{key}
- POST /api/customers/{customerId}/consent
- GET /api/customers/{customerId}/audit-log
- POST /api/customers/{customerId}/context/export
- DELETE /api/customers/{customerId}/context
- GET /api/context-sync/watch?customerId=X
