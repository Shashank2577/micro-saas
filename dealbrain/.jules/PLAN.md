# Execution Plan

1. Generate Entities (dealbrain/backend/src/main/java/com/microsaas/dealbrain/model)
   - Deal.java
   - DealActivity.java
   - DealRiskSignal.java
   - DealRecommendation.java
   - Stakeholder.java
   - HistoricalDeal.java

2. Generate Repositories (dealbrain/backend/src/main/java/com/microsaas/dealbrain/repository)
   - DealRepository.java
   - DealActivityRepository.java
   - DealRiskSignalRepository.java
   - DealRecommendationRepository.java
   - StakeholderRepository.java
   - HistoricalDealRepository.java

3. Generate Services (dealbrain/backend/src/main/java/com/microsaas/dealbrain/service)
   - DealHealthScoringService.java
   - CloseProbabilityService.java
   - RiskSignalDetectionService.java
   - NextActionRecommendationService.java
   - CrmSyncService.java
   - EmailEngagementService.java

4. Generate Controllers (dealbrain/backend/src/main/java/com/microsaas/dealbrain/controller)
   - DealController.java
   - PipelineController.java

5. Database Migration (dealbrain/backend/src/main/resources/db/migration)
   - Delete old V1 file, create V1__init.sql

6. Generate Tests (dealbrain/backend/src/test/java/com/microsaas/dealbrain/service)
   - Test classes for all services with JUnit + Mockito

7. Frontend (dealbrain/frontend)
   - Add dashboard page at src/app/dashboard/page.tsx
   - Add API fetch logic for deals/pipeline
   - Ensure vitest tests are updated if necessary

8. Integration manifest
   - Update integration-manifest.json

9. Verification
   - mvn -pl dealbrain/backend clean verify
   - npm --prefix dealbrain/frontend test
