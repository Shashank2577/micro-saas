1. **Explore and scaffold frontend app**
    - Ensure frontend `Next.js` app has all required pages. Create using a `npx create-next-app` style approach or whatever standard is used in this repo.
2. **Implement Missing Backend Entities and Repositories**
    - Create `PolicyDraft.java` (AI-assisted policy drafting).
    - Create `PolicyGap.java` (gaps detected via AI).
    - Create `UpdateSummary.java` (AI summary of changes).
    - Create `PolicyCategory.java` (tags/categories).
    - Create `IncidentReference.java` (links to external incidents/audits).
    - Create corresponding Spring Data JPA repositories.
3. **Update Flyway Migration**
    - Update `V1__init.sql` (or create `V2__add_entities.sql`) to include the new tables for the new entities.
4. **Implement AI Service Logic (AI Pattern)**
    - Implement an `AiPolicyService.java` using `com.crosscutting.ai.LiteLlmClient` (or similar interface from `cc-starter`).
    - Implement `generateDraft` (co-pilot writing).
    - Implement `generateUpdateSummary` (diff summary between versions).
    - Implement `analyzeGaps` (RAG cross-referencing incidents/audits).
5. **Implement Missing Backend Endpoints**
    - Update or create controllers for Drafts, Gaps, Categories, Incidents, and AI features.
6. **Integrate Webhooks**
    - Emit `policy.published` and `policy.acknowledged` using `WebhookService`.
7. **Create Docker artifacts**
    - Ensure `Dockerfile` exists in `backend/`.
    - Ensure `docker-compose.yml` exists in `policyforge/`.
8. **Update `integration-manifest.json`**
    - Ensure it matches the actual implementation 100%.
9. **Build and Test Verification**
    - Run `mvn clean install` for backend tests.
    - Write specific JUnit tests using Mockito for new AI and service logic.
10. **Pre-commit and submission**
    - Complete pre-commit steps to ensure proper testing, verification, review, and reflection are done.
