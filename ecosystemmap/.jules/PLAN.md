1. Create and commit SPEC.md and PLAN.md to satisfy the GATE.
2. Update backend/pom.xml with required dependencies (cc-starter, postgresql, flyway).
3. Implement backend core entities (Ecosystem, Node, Connection, RoiMetric, DataFlowEvent, DeploymentStatus, EcosystemSnapshot, AiInsight).
4. Implement Spring Data JPA Repositories for entities.
5. Implement backend services (EcosystemService, NodeService, ConnectionService, RoiTrackingService, EventProcessingService, AiInsightService, VisualizationService) including LiteLLM integration.
6. Implement backend controllers (EcosystemController, NodeController, ConnectionController, RoiMetricController, AiInsightController).
7. Create Flyway migration V1__init.sql and Dockerfile in backend/.
8. Create frontend scaffolding and pages for entities using Next.js.
9. Create docker-compose.yml in ecosystemmap/ and update integration-manifest.json.
10. Run backend and frontend tests to verify the implementation.
11. Complete pre-commit steps to ensure proper testing, verification, review, and reflection are done.
12. Create HANDOFF.md and submit PR.
