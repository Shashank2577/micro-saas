# Implementation Plan

1. **Phase 1: Planning**: Write SPEC.md and PLAN.md, and commit them.
2. **Phase 2: Database & Entities**: Update Flyway `V1__init.sql`. Create Entity classes and Repositories.
3. **Phase 3: Services**: Split existing `AuditVaultService` into 7+ domain services. Create `AiMappingService` integrating LiteLLM.
4. **Phase 4: Controllers**: Create 5+ REST controllers for the domain models.
5. **Phase 5: Frontend**: Scaffold Next.js pages for the entities.
6. **Phase 6: Docker**: Update `Dockerfile` and create `docker-compose.yml`.
7. **Phase 7: Verification**: Run tests, check manifest, pre-commit steps.
