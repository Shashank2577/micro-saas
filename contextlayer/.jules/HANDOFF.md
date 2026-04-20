# Handoff Notes: ContextLayer

## What was built
- **Backend:** Setup all domain entities (`CustomerContext`, `ContextVersion`, `InteractionHistory`, `CustomerPreference`, `ConsentRecord`), integrated with Spring Data JPA.
- **Services:** High performance services mapping the entire architecture outlined in the Work Order. Real-time pgmq queue syncing implemented in `RealtimeSyncService`. LiteLLM orchestrated inside `PreferenceLearningService`.
- **API:** The `/api/customers/{customerId}/*` REST boundaries created and covered using mocked MVC context.
- **Frontend:** All specified pages generated using Next.js App Router (Dashboard, Customer list, Customer viewer) and compiled successfully.
- **Verification:** All backend unit/integration tests pass. Frontend static rendering passes successfully without TypeErrors.

## Questions & Assumptions
- *Question:* Should LTV/Churn logic inside `ContextEnrichmentService` use AI or standard formulas? *Assumption:* Opted for a placeholder string `{"segment":"default"}` pending explicit formula logic from external providers.
- *Question:* Should context rolling back fully purge newer snapshots? *Assumption:* Implemented standard overwrite logic leaving the version history intact for auditability instead of destructive rollback.
- *Assumption:* Ignored authentication constraints at the controller level during tests (disabled MockMvc filters to test isolated endpoints). Production needs the `cc-starter` security modules explicitly enabled via properties.

## Future Work
- Implement actual Kafka/Redis integration if real-time needs exceed pgmq limitations.
- Expand React UI to map complex JSONB Context properties into dynamic trees via `react-json-view`.
- Setup proper Docker/compose deployment profiles matching exact local cluster setups.
