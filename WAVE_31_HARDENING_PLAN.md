# Wave-31 Hardening Plan

## Objective

Harden 60+ production-ready apps across 4 improvement areas:

| Wave | Sessions | Focus | Apps Covered |
|------|----------|-------|--------------|
| A | 1–4 | cc-starter injection (18 apps missing it) | 18 |
| B | 5–16 | Service-layer unit tests | ~35 |
| C | 17–24 | Dockerfile + docker-compose | ~30 |
| D | 25–28 | Architecture fixes | 6 |
| E | 29–30 | LiteLLM wiring + integration tests | 8 |

**Excluded (stubs):** contentos, ethicsmonitor, jobcraftai, seointelligence, budgetpilot, constructioniq, deploysignal, securitypulse

---

## cc-starter capabilities (auto-enabled on dependency + application.yml config)

- `cc.auth.*` → Spring Security OAuth2 Resource Server (Keycloak JWT)
- `cc.tenancy.*` → Multi-tenant filter
- `GlobalExceptionHandler` → Structured error responses
- `OpenApiConfig` → Swagger/OpenAPI 3
- `spring-boot-starter-validation` → @Valid support
- `RateLimitFilter` → Redis-backed rate limiting

**Reference config block (add to any app's application.yml):**
```yaml
cc:
  tenancy:
    mode: header
    header-name: X-Tenant-ID
  auth:
    keycloak-url: ${KEYCLOAK_URL:http://localhost:8180}
    realm: ${KEYCLOAK_REALM:cc}
    client-id: ${KEYCLOAK_CLIENT_ID:cc-backend}
  ai:
    gateway-url: ${LITELLM_URL:http://localhost:4000}
    api-key: ${LITELLM_API_KEY:sk-local-dev-key}
    default-model: claude-sonnet-4-6
```

**Reference docker-compose.yml template:**
```yaml
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: ecosystem
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
  {app}:
    build: ./backend
    ports:
      - "{port}:8080"
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/ecosystem
      DB_USER: postgres
      DB_PASS: postgres
      KEYCLOAK_URL: http://keycloak:8180
      KEYCLOAK_REALM: cc
      KEYCLOAK_CLIENT_ID: cc-backend
      LITELLM_URL: http://litellm:4000
    depends_on:
      - postgres
      - redis
```

---

## Session Definitions

### WAVE A — cc-starter Injection (Sessions 1–4)

**Task per session:** For each app listed:
1. Verify `cc-starter` is in pom.xml; if not add it with `<groupId>com.crosscutting</groupId> <artifactId>cc-starter</artifactId> <version>0.1.0-SNAPSHOT</version>`
2. Add the cc config block above to application.yml if not present
3. Add `spring.flyway.schemas: tenant,cc,audit` to application.yml if not present
4. Add `spring.security.oauth2.resourceserver.jwt.issuer-uri: ${KEYCLOAK_URL:http://localhost:8180}/realms/${KEYCLOAK_REALM:cc}` to application.yml
5. Ensure no existing `@EnableWebSecurity` class conflicts with cc-starter's SecurityConfig
6. Verify the app compiles after changes

**Session A1:** hiresignal, apievolver, compbenchmark, creatoranalytics, notificationhub
**Session A2:** peopleanalytics, brandvoice, socialintelligence, ndaflow, localizationos
**Session A3:** retentionsignal, dependencyradar, invoiceprocessor, runwaymodeler, policyforge
**Session A4:** dataqualityai, supportintelligence + fix Swagger path conflicts in all 18 apps

---

### WAVE B — Service-Layer Tests (Sessions 5–16)

**Task per session:** For each app listed, write unit tests that:
- Test each `@Service` class independently with mocked repositories
- Cover the happy path + at least one error/edge case per method
- Use `@ExtendWith(MockitoExtension.class)` pattern
- Aim for 3–5 test methods per service
- Place tests in `src/test/java/.../service/`
- Existing tests must still pass

**Session B1 (financial critical):** debtnavigator, billingai
**Session B2:** interviewos, cashflowanalyzer, contractportfolio
**Session B3:** equityintelligence, financenarrator, regulatoryfiling
**Session B4:** competitorradar, hiresignal, procurebot
**Session B5:** compensationos, copyoptimizer, apievolver, performancenarrative
**Session B6:** cashflowai, billingsync, apimanager, apigatekeeper
**Session B7:** churnpredictor, wealthplan, retirementplus
**Session B8:** vendormonitor, logisticsai, restaurantintel, educationos
**Session B9:** complianceradar, onboardflow, engagementpulse, creatoranalytics
**Session B10:** identitycore, authvault, processminer, workspacemanager
**Session B11:** legalresearch, cacheoptimizer, datastoryteller
**Session B12:** dataunification, documentvault, insightengine, prospectiq

---

### WAVE C — Dockerfile + docker-compose (Sessions 17–24)

**Task per session:** For each app listed:
1. If `backend/Dockerfile` missing: create it using the standard pattern (eclipse-temurin:21, multi-stage, EXPOSE 8080)
2. If `docker-compose.yml` missing in app root: create one with postgres:16 + redis:7 + the app itself
3. Use env vars for all connection strings (DB_URL, KEYCLOAK_URL, LITELLM_URL)
4. Set `depends_on: [postgres, redis]`
5. Add `healthcheck` to postgres service

**Session C1 (Tier 1 missing Docker):** debtnavigator, compensationos, competitorradar, cashflowanalyzer, apievolver
**Session C2:** wealthplan, cashflowai, workspacemanager, notificationhub, apimanager
**Session C3:** integrationbridge, integrationmesh, datastoryteller, agencyos, peopleanalytics
**Session C4:** meetingbrain, ecosystemmap, taskqueue, experimentengine, insightengine
**Session C5:** dataroomai, nexus-hub, careerpath, dataunification, documentvault
**Session C6:** restaurantintel, retailintelligence, insuranceai, investtracker, prospectiq
**Session C7:** taxoptimizer, taxdataorganizer, healthcaredocai, wealthedge, marketsignal
**Session C8:** ghostwriter, videonarrator, auditready, retentionsignal, observabilityai

---

### WAVE D — Architecture Fixes (Sessions 25–28)

**Session D1 — observabilitystack:**
- Add a `service/` layer with `@Service` classes that the 7 controllers delegate to
- Move all business logic from controllers into services
- Each controller method should be ≤5 lines (call service, return response)

**Session D2 — careerpath + peopleanalytics:**
- careerpath: Add `CareerRecommendationController` that exposes the `PathService` and `SkillService`; add `GapAnalysisController` for skill gap endpoints
- peopleanalytics: Add `HeadcountController`, `EngagementController`, `TurnoverController` to expose the 9 existing services

**Session D3 — experimentengine + telemetrycore + localizationos:**
- experimentengine: Add `ExperimentController` and `AssignmentController` that wire to existing services
- telemetrycore: Add `MetricsIngestController` and `AlertController`
- localizationos: Add `TranslationController` and wire to LiteLLM for translation

**Session D4 — meetingbrain + integrationmesh + datastoryteller:**
- meetingbrain: Expose `MeetingService` and `TranscriptService` via additional controller methods
- integrationmesh: Add `EventRoutingController`, `ConnectorController` alongside existing
- datastoryteller: Wire LiteLLM `ai.gateway-url` into `StoryService.generateNarrative()`

---

### WAVE E — LiteLLM Wiring + Integration Tests (Sessions 29–30)

**Task:** For each app that has `cc.ai.gateway-url` in application.yml but no tests for AI calls:
1. Verify `AiService` or equivalent is calling `${cc.ai.gateway-url}/chat/completions`
2. Add `@MockBean` or `WireMock` stub test for the LiteLLM call
3. Add `application-test.yml` with `cc.ai.gateway-url: http://localhost:${wiremock.port}`
4. Test both success path (mock returns a story/analysis) and failure path (503 → graceful error)

**Session E1:** retirementplus, wealthplan, legalresearch, analyticsbuilder
**Session E2:** performancenarrative, cashflowanalyzer, churnpredictor, copyoptimizer

---

## Dispatch Order

Sessions dispatched across 3 API keys (10 per key):
- Key 1 (Shash2577): Sessions A1, A2, B1, B2, B3, B4, B5, B6, B7, C1
- Key 2 (shashank.saxena91): Sessions A3, A4, B8, B9, B10, B11, B12, C2, C3, C4
- Key 3 (shash.akash.aibot): Sessions C5, C6, C7, C8, D1, D2, D3, D4, E1, E2
