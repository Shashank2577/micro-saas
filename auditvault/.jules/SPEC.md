# AuditVault SPEC
1. **Goal**: AI compliance evidence aggregation platform.
2. **Backend**: Needs 8+ entities, 7+ services, 5+ controllers extending cc-starter.
   - **Entities**: Framework, Control, Evidence, EvidenceMapping, AuditPackage, EvidenceRequest, ComplianceFinding, FrameworkRequirement.
   - **Services**: FrameworkService, ControlService, EvidenceService, EvidenceMappingService, AuditPackageService, EvidenceRequestService, ComplianceFindingService, AiMappingService.
   - **Controllers**: FrameworkController, ControlController, EvidenceController, EvidenceMappingController, AuditPackageController, EvidenceRequestController, ComplianceFindingController.
3. **AI**: AiMappingService uses `com.crosscutting.starter.ai.AiService` to map evidence to controls.
4. **Database**: `V1__init.sql` covers all 8 entities.
5. **Docker**: `backend/Dockerfile` builds using maven with cross-cutting context. `docker-compose.yml` sets up Postgres, Redis, backend, frontend.
6. **Frontend**: Next.js App Router. Pages for frameworks, controls, evidence, packages, mappings.
7. **Integration**: `integration-manifest.json` updated with new models.
