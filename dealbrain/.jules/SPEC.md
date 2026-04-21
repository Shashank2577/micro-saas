# DealBrain SPEC

## Entities
- Deal (id, tenantId, createdAt, updatedAt)
- DealActivity (id, tenantId, dealId, type, timestamp, createdAt, updatedAt)
- DealRiskSignal (id, tenantId, dealId, signalType, severity, detectedAt, createdAt, updatedAt)
- DealRecommendation (id, tenantId, dealId, action, reason, createdAt, updatedAt)
- Stakeholder (id, tenantId, dealId, role, engagementLevel, createdAt, updatedAt)
- HistoricalDeal (id, tenantId, dealId, closeDate, outcome, createdAt, updatedAt)

## Services
- DealHealthScoringService
- CloseProbabilityService
- RiskSignalDetectionService
- NextActionRecommendationService
- CrmSyncService
- EmailEngagementService

## Endpoints
- GET /api/deals/{id}/health-score
- GET /api/deals/{id}/close-probability
- GET /api/deals/{id}/risk-signals
- GET /api/deals/{id}/recommendations
- GET /api/pipeline/dashboard
- POST /api/deals/{id}/activities
