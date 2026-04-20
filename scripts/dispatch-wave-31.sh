#!/usr/bin/env bash
# Wave-31 Hardening Dispatch — 30 Jules sessions across 3 API keys
# Usage: bash scripts/dispatch-wave-31.sh

set -euo pipefail

REPO="Shashank2577/micro-saas"
KEY1="${JULES_API_KEY_1:-}"
KEY2="${JULES_API_KEY_2:-}"
KEY3="${JULES_API_KEY_3:-}"
API="https://jules.googleapis.com/v1alpha/sessions"
LOG=".jules-wave-31-dispatch.log"
STATE=".jules-wave-31.state"

: > "$STATE"
echo "Wave-31 dispatch started at $(date -u)" | tee "$LOG"

dispatch() {
  local key="$1" session_name="$2" prompt="$3"
  local payload response session_id
  payload=$(python3 -c "
import json, sys
data = {
  'repository': '$REPO',
  'title': sys.argv[1],
  'description': sys.argv[2],
  'autoMerge': False
}
print(json.dumps(data))
" "$session_name" "$prompt" 2>/dev/null || echo "{}")

  response=$(curl -sS --connect-timeout 10 --max-time 30 \
    -X POST \
    -H "X-Goog-Api-Key: $key" \
    -H "Content-Type: application/json" \
    "$API" \
    -d "{\"prompt\": $(echo "$prompt" | python3 -c 'import json,sys; print(json.dumps(sys.stdin.read()))')}" \
    2>/dev/null)

  session_id=$(echo "$response" | python3 -c "import json,sys; d=json.load(sys.stdin); print(d.get('name','').split('/')[-1])" 2>/dev/null || echo "FAILED")

  if [[ "$session_id" =~ ^[0-9]+$ ]]; then
    echo "${session_name}=${session_id}" | tee -a "$STATE"
    echo "[OK] $session_name => $session_id" >> "$LOG"
  else
    echo "${session_name}=FAILED" | tee -a "$STATE"
    echo "[FAIL] $session_name: $response" >> "$LOG"
  fi
  sleep 3
}

CC_CONFIG_BLOCK='cc:
  tenancy:
    mode: header
    header-name: X-Tenant-ID
  auth:
    keycloak-url: \${KEYCLOAK_URL:http://localhost:8180}
    realm: \${KEYCLOAK_REALM:cc}
    client-id: \${KEYCLOAK_CLIENT_ID:cc-backend}
  ai:
    gateway-url: \${LITELLM_URL:http://localhost:4000}
    api-key: \${LITELLM_API_KEY:sk-local-dev-key}
    default-model: claude-sonnet-4-6'

AUTONOMOUS_HEADER="You are an autonomous software engineer working in the Shashank2577/micro-saas repository. Work completely autonomously - make all decisions yourself, document them in the app's .jules/ folder, do NOT ask for feedback or approval. When done, open a single PR with all changes. Follow the patterns in cross-cutting/cc-starter/ for all implementations."

DOCKERFILE_TEMPLATE='FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]'

echo "=== WAVE A: cc-starter Injection ==="

# A1
dispatch "$KEY1" "wave31-A1-ccstarter" \
"$AUTONOMOUS_HEADER

TASK: Add cc-starter dependency and configure it for these 5 apps: hiresignal, apievolver, compbenchmark, creatoranalytics, notificationhub

For EACH app:

1. Open {app}/backend/pom.xml. If <artifactId>cc-starter</artifactId> is NOT present, add this dependency inside <dependencies>:
   <dependency>
     <groupId>com.crosscutting</groupId>
     <artifactId>cc-starter</artifactId>
     <version>0.1.0-SNAPSHOT</version>
   </dependency>

2. Open {app}/backend/src/main/resources/application.yml. Add this block if a 'cc:' section does not exist:
   cc:
     tenancy:
       mode: header
       header-name: X-Tenant-ID
     auth:
       keycloak-url: \${KEYCLOAK_URL:http://localhost:8180}
       realm: \${KEYCLOAK_REALM:cc}
       client-id: \${KEYCLOAK_CLIENT_ID:cc-backend}
     ai:
       gateway-url: \${LITELLM_URL:http://localhost:4000}
       api-key: \${LITELLM_API_KEY:sk-local-dev-key}
       default-model: claude-sonnet-4-6

3. Add to spring.flyway section (if not present): schemas: tenant,cc,audit

4. Add to spring.security section (if not present):
   oauth2:
     resourceserver:
       jwt:
         issuer-uri: \${KEYCLOAK_URL:http://localhost:8180}/realms/\${KEYCLOAK_REALM:cc}

5. If any of these apps have their OWN @EnableWebSecurity or SecurityConfig class that conflicts with cc-starter's SecurityConfig, delete it (cc-starter provides SecurityConfig via autoconfiguration).

6. Verify each app's main Application class is annotated with @SpringBootApplication only (no conflicting @Import for security).

Look at churnpredictor/backend/src/main/resources/application.yml and retirementplus/backend/pom.xml as reference examples of correctly configured apps.

PR title: 'feat(security): wire cc-starter auth into hiresignal, apievolver, compbenchmark, creatoranalytics, notificationhub'"

# A2
dispatch "$KEY1" "wave31-A2-ccstarter" \
"$AUTONOMOUS_HEADER

TASK: Add cc-starter dependency and configure it for these 5 apps: peopleanalytics, brandvoice, socialintelligence, ndaflow, localizationos

For EACH app follow the exact same steps:
1. Add cc-starter to pom.xml if missing (groupId: com.crosscutting, artifactId: cc-starter, version: 0.1.0-SNAPSHOT)
2. Add cc: config block to application.yml (tenancy + auth + ai sections with env var defaults)
3. Add spring.flyway.schemas: tenant,cc,audit
4. Add spring.security.oauth2.resourceserver.jwt.issuer-uri config
5. Remove any conflicting local SecurityConfig/@EnableWebSecurity if present

Reference: look at churnpredictor/backend/ for a complete correctly configured app.

PR title: 'feat(security): wire cc-starter auth into peopleanalytics, brandvoice, socialintelligence, ndaflow, localizationos'"

# A3
dispatch "$KEY2" "wave31-A3-ccstarter" \
"$AUTONOMOUS_HEADER

TASK: Add cc-starter dependency and configure it for these 5 apps: retentionsignal, dependencyradar, invoiceprocessor, runwaymodeler, policyforge

For EACH app:
1. Add cc-starter to pom.xml if missing
2. Add cc: config block to application.yml
3. Note: dependencyradar has NO frontend directory - skip frontend changes entirely
4. Note: invoiceprocessor has NO frontend directory - skip frontend changes entirely
5. Note: runwaymodeler has NO frontend directory - skip frontend changes entirely
6. Add flyway schemas config, add spring security oauth2 resourceserver jwt config
7. Remove conflicting SecurityConfig if present

Reference: churnpredictor/backend/ for correct configuration.

PR title: 'feat(security): wire cc-starter auth into retentionsignal, dependencyradar, invoiceprocessor, runwaymodeler, policyforge'"

# A4
dispatch "$KEY2" "wave31-A4-ccstarter" \
"$AUTONOMOUS_HEADER

TASK: Add cc-starter dependency and configure it for these 3 apps: dataqualityai, supportintelligence, hiresignal (verify hiresignal from A1 compiled correctly - re-do if needed)

Also for ALL 18 apps that now have cc-starter (hiresignal, apievolver, compbenchmark, creatoranalytics, notificationhub, peopleanalytics, brandvoice, socialintelligence, ndaflow, localizationos, retentionsignal, dependencyradar, invoiceprocessor, runwaymodeler, policyforge, dataqualityai, supportintelligence), verify that springdoc is configured:

Add to each app's application.yml if missing:
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html

PR title: 'feat(security): complete cc-starter wiring for dataqualityai, supportintelligence + springdoc for all wave-A apps'"

echo ""
echo "=== WAVE B: Service-Layer Tests ==="

# B1 - Financial critical
dispatch "$KEY1" "wave31-B1-tests-financial" \
"$AUTONOMOUS_HEADER

TASK: Write comprehensive unit tests for the service layer of debtnavigator and billingai. These are financial applications so correctness is critical.

For debtnavigator:
- Look at all files in debtnavigator/backend/src/main/java/com/microsaas/debtnavigator/service/
- For EACH service class, create a corresponding test class in debtnavigator/backend/src/test/java/com/microsaas/debtnavigator/service/
- Use @ExtendWith(MockitoExtension.class), @Mock for repositories, @InjectMocks for the service
- Write minimum 4 test methods per service: happy path, validation error, not-found scenario, edge case
- Pay special attention to debt calculation methods - test with actual numbers

For billingai:
- Look at all files in billingai/backend/src/main/java/com/microsaas/billingai/service/
- Same pattern: @ExtendWith(MockitoExtension.class)
- CRITICAL: For any payment/invoice/charge calculation methods, test with exact decimal values
- Add idempotency key test: calling the same billing operation twice should not double-charge
- Test failure paths: what happens when payment fails, when invoice not found

Look at existing test files in these apps first to match the existing test style.
Run: verify the tests compile (do not run them - just ensure they are syntactically correct).

PR title: 'test(financial): comprehensive service tests for debtnavigator and billingai'"

# B2
dispatch "$KEY1" "wave31-B2-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: interviewos, cashflowanalyzer, contractportfolio

Pattern for each app:
- Find all @Service classes in {app}/backend/src/main/java/.../service/
- Create test class per service in src/test/java/.../service/
- Use @ExtendWith(MockitoExtension.class), mock all @Autowired repositories with @Mock
- Write 3-5 test methods per service: createX_success, createX_duplicateThrows, updateX_notFoundThrows, getX_returnsFiltered, deleteX_cascades
- Use Mockito.when().thenReturn() for repository mocks
- Use assertThrows() for exception cases

For cashflowanalyzer specifically: test the LiteLLM AI call path. Mock the RestTemplate/WebClient that calls cc.ai.gateway-url and verify the service handles both successful AI response and AI service unavailable (exception).

PR title: 'test: service layer tests for interviewos, cashflowanalyzer, contractportfolio'"

# B3
dispatch "$KEY1" "wave31-B3-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: equityintelligence, financenarrator, regulatoryfiling

For equityintelligence: financial portfolio data - test portfolio value calculations, verify tenantId isolation (assert service only returns data for the correct tenantId), test trade execution validation.

For financenarrator: ZERO tests currently - this is the highest priority. Write tests for ALL service methods. This app generates financial narratives via LiteLLM - mock the AI call and test that the narrative is assembled correctly from the input data.

For regulatoryfiling: compliance deadlines are critical. Test: filing deadline calculation, overdue detection, filing status transitions (DRAFT → SUBMITTED → ACCEPTED/REJECTED).

PR title: 'test: service layer tests for equityintelligence, financenarrator, regulatoryfiling'"

# B4
dispatch "$KEY1" "wave31-B4-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: competitorradar, hiresignal, procurebot

For competitorradar: 51 Java files but only 1 test. Test all 8 service classes. Focus on: competitor data ingestion, price change detection, market position calculation.

For hiresignal: recruitment signal scoring. Test: candidate scoring algorithm, signal aggregation, interview scheduling conflict detection.

For procurebot: procurement workflows. Test: purchase order approval state machine (DRAFT→APPROVED→REJECTED), vendor selection logic, budget limit enforcement (reject PO that exceeds budget).

PR title: 'test: service layer tests for competitorradar, hiresignal, procurebot'"

# B5
dispatch "$KEY1" "wave31-B5-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: compensationos, copyoptimizer, apievolver, performancenarrative

For compensationos: salary band logic is critical. Test: is-salary-in-band?, comp ratio calculation, equity grant vesting schedule. Ensure test for role-based access: non-HR users should not see other employees' salaries (mock SecurityContext).

For copyoptimizer: AI-driven content. Test the service that builds the LiteLLM prompt from user input. Test response parsing. Test character/token limit enforcement.

For apievolver: API versioning logic. Test version negotiation, deprecation detection, breaking change analysis.

For performancenarrative: LiteLLM narrative generation. Mock the AI call, test the metrics-to-prompt assembly.

PR title: 'test: service layer tests for compensationos, copyoptimizer, apievolver, performancenarrative'"

# B6
dispatch "$KEY2" "wave31-B6-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: cashflowai, billingsync, apimanager, apigatekeeper

For cashflowai: cashflow projection calculations. Test: burn rate at N months, runway calculation, scenario comparison (base/optimistic/pessimistic).

For billingsync: sync idempotency is critical. Test: syncing the same invoice twice doesn't create duplicates, conflict resolution when local and remote differ, error state recovery.

For apimanager: API key management. Test: key generation uniqueness, key revocation, key rotation.

For apigatekeeper: request validation. Test: blocked request patterns, allowed request passthrough, rate limit enforcement.

PR title: 'test: service layer tests for cashflowai, billingsync, apimanager, apigatekeeper'"

# B7
dispatch "$KEY2" "wave31-B7-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: churnpredictor, wealthplan, retirementplus

For churnpredictor: this app already has AI service calls (AiOfferService, PredictionService). Mock the LiteLLM endpoint. Test: churn score threshold (score > 0.7 triggers intervention), intervention selection logic, offer generation prompt assembly.

For wealthplan: financial projections. Test: compound growth calculation, Monte Carlo simulation parameters, asset allocation rebalancing trigger logic.

For retirementplus: This app has a real LiteLLMClient.java. Test it with a mocked HTTP server (WireMock or MockRestServiceServer). Test: retirement age calculation, withdrawal rate safety check (4% rule), Social Security optimization.

PR title: 'test: service layer tests for churnpredictor, wealthplan, retirementplus'"

# B8
dispatch "$KEY2" "wave31-B8-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: vendormonitor, logisticsai, restaurantintel, educationos

For vendormonitor: already has 9 tests (best in class) - add 3-5 more for: SLA breach detection timing, vendor risk score recalculation trigger, alert deduplication.

For logisticsai: route optimization is the core. Test: shortest path selection, delivery window compliance, carrier capacity constraint.

For restaurantintel: Test: menu item profitability calculation, peak hours detection from order data, food cost variance alerts.

For educationos: Test: course completion percentage calculation, quiz score grading, student progress milestone detection.

PR title: 'test: service layer tests for vendormonitor, logisticsai, restaurantintel, educationos'"

# B9
dispatch "$KEY2" "wave31-B9-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: complianceradar, onboardflow, engagementpulse, creatoranalytics

For complianceradar: compliance rule evaluation. Test: rule PASS/FAIL evaluation, severity scoring, remediation priority ordering.

For onboardflow: onboarding state machine. Test: checklist completion percentage, step dependency (can't complete step 3 before step 2), milestone detection.

For engagementpulse: engagement metric aggregation. Test: NPS calculation (promoters - detractors), engagement score rolling average, segment comparison.

For creatoranalytics: content performance. Test: engagement rate calculation (likes+comments/impressions), top content ranking, growth rate calculation.

PR title: 'test: service layer tests for complianceradar, onboardflow, engagementpulse, creatoranalytics'"

# B10
dispatch "$KEY2" "wave31-B10-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: identitycore, authvault, processminer, workspacemanager

For identitycore: this IS the authentication service. Test: user registration validation, password policy enforcement, role assignment, permission check (has user X permission Y in tenant Z?).

For authvault: credential storage. Test: secret encryption before storage (verify plaintext is never stored), secret retrieval decryption, secret versioning (previous version still accessible after rotation).

For processminer: process analysis. Test: process step duration calculation, bottleneck detection (step with max avg duration), process variant clustering.

For workspacemanager: workspace isolation. Test: member invite flow, workspace resource limits, member role assignment.

PR title: 'test: service layer tests for identitycore, authvault, processminer, workspacemanager'"

# B11
dispatch "$KEY2" "wave31-B11-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: legalresearch, cacheoptimizer, datastoryteller

For legalresearch: LiteLLM is wired. Test: case citation extraction from AI response, jurisdiction filter logic, precedent relevance scoring. Mock the AI gateway call with WireMock or MockRestServiceServer.

For cacheoptimizer: cache strategy logic. Test: eviction policy recommendation based on hit/miss ratio, cache size recommendation based on access patterns, Redis TTL calculation.

For datastoryteller: this app has 8 repos but thin narrative service. Test: data → insight aggregation, insight → narrative prompt assembly, LiteLLM call mocking.

PR title: 'test: service layer tests for legalresearch, cacheoptimizer, datastoryteller'"

# B12
dispatch "$KEY3" "wave31-B12-tests" \
"$AUTONOMOUS_HEADER

TASK: Write unit tests for the service layer of: dataunification, documentvault, insightengine, prospectiq

For dataunification: field mapping and deduplication. Test: duplicate record detection by key fields, field mapping transformation, conflict resolution (prefer newer record).

For documentvault: document access control. Test: ACL enforcement (user without permission gets 403), document encryption flag enforcement, audit log entry creation on access.

For insightengine: insight generation. Test: anomaly threshold detection, insight categorization, trend direction calculation.

For prospectiq: lead scoring. Test: lead score calculation from firmographic data, ICP match percentage, outreach priority ordering.

PR title: 'test: service layer tests for dataunification, documentvault, insightengine, prospectiq'"

echo ""
echo "=== WAVE C: Dockerfile + docker-compose ==="

DOCKER_TASK_TEMPLATE() {
local apps="$1"
local pr_apps="$2"
echo "$AUTONOMOUS_HEADER

TASK: Add Dockerfile and docker-compose.yml to these apps: $apps

For EACH app:

1. If {app}/backend/Dockerfile does NOT exist, create it:
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]

2. Check if backend/mvnw exists - if not, look at another app that has it (e.g. debtnavigator/backend/mvnw) and note that mvnw should be present (do not create it - just verify).

3. Create {app}/docker-compose.yml (in the APP root, not in backend/):
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: ecosystem
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - '5432:5432'
    healthcheck:
      test: ['CMD-SHELL', 'pg_isready -U postgres']
      interval: 10s
      timeout: 5s
      retries: 5
  redis:
    image: redis:7-alpine
    ports:
      - '6379:6379'
  {app}:
    build: ./backend
    ports:
      - '{PORT}:8080'
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/ecosystem
      DB_USER: postgres
      DB_PASS: postgres
      KEYCLOAK_URL: http://host.docker.internal:8180
      KEYCLOAK_REALM: cc
      KEYCLOAK_CLIENT_ID: cc-backend
      LITELLM_URL: http://host.docker.internal:4000
      LITELLM_API_KEY: sk-local-dev-key
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_started

Replace {PORT} with the app's actual server.port from application.yml.

PR title: 'infra: add Dockerfile and docker-compose for $pr_apps'"
}

# C1
dispatch "$KEY3" "wave31-C1-docker" "$(DOCKER_TASK_TEMPLATE "debtnavigator, compensationos, competitorradar, cashflowanalyzer, apievolver" "debtnavigator, compensationos, competitorradar, cashflowanalyzer, apievolver")"

# C2
dispatch "$KEY3" "wave31-C2-docker" "$(DOCKER_TASK_TEMPLATE "wealthplan, cashflowai, workspacemanager, notificationhub, apimanager" "wealthplan, cashflowai, workspacemanager, notificationhub, apimanager")"

# C3
dispatch "$KEY3" "wave31-C3-docker" "$(DOCKER_TASK_TEMPLATE "integrationbridge, integrationmesh, datastoryteller, agencyos, peopleanalytics" "integrationbridge, integrationmesh, datastoryteller, agencyos, peopleanalytics")"

# C4
dispatch "$KEY3" "wave31-C4-docker" "$(DOCKER_TASK_TEMPLATE "meetingbrain, ecosystemmap, taskqueue, experimentengine, insightengine" "meetingbrain, ecosystemmap, taskqueue, experimentengine, insightengine")"

# C5
dispatch "$KEY3" "wave31-C5-docker" "$(DOCKER_TASK_TEMPLATE "dataroomai, nexus-hub, careerpath, dataunification, documentvault" "dataroomai, nexus-hub, careerpath, dataunification, documentvault")"

# C6
dispatch "$KEY3" "wave31-C6-docker" "$(DOCKER_TASK_TEMPLATE "restaurantintel, retailintelligence, insuranceai, investtracker, prospectiq" "restaurantintel, retailintelligence, insuranceai, investtracker, prospectiq")"

# C7
dispatch "$KEY3" "wave31-C7-docker" "$(DOCKER_TASK_TEMPLATE "taxoptimizer, taxdataorganizer, healthcaredocai, wealthedge, marketsignal" "taxoptimizer, taxdataorganizer, healthcaredocai, wealthedge, marketsignal")"

# C8
dispatch "$KEY3" "wave31-C8-docker" "$(DOCKER_TASK_TEMPLATE "ghostwriter, videonarrator, auditready, retentionsignal, observabilityai" "ghostwriter, videonarrator, auditready, retentionsignal, observabilityai")"

echo ""
echo "=== WAVE D: Architecture Fixes ==="

# D1
dispatch "$KEY1" "wave31-D1-arch-observabilitystack" \
"$AUTONOMOUS_HEADER

TASK: Fix observabilitystack architecture — add service layer between controllers and repositories.

Current problem: observabilitystack has 7 controllers calling 12 repositories directly. This violates layered architecture and makes testing impossible.

Steps:
1. Look at all controller files in observabilitystack/backend/src/main/java/com/microsaas/observabilitystack/controller/
2. For each controller, identify the business logic currently in the controller methods
3. Create a corresponding @Service class in .../service/ that contains that logic
4. Refactor each controller to: inject the service, delegate to service, return ResponseEntity
5. Controller methods should be max 5 lines after refactoring
6. Add @Transactional on service methods that write to the DB
7. Write 2 unit tests per new service class

Also: the observabilitystack has 0 services currently. After this change it should have 5+ services.

PR title: 'refactor(observabilitystack): introduce service layer between controllers and repositories'"

# D2
dispatch "$KEY1" "wave31-D2-arch-careerpath-people" \
"$AUTONOMOUS_HEADER

TASK: Expose buried services via new controllers in careerpath and peopleanalytics.

For careerpath:
- Currently has 8 repositories and 2 services but only 1 controller
- Look at all service classes in careerpath/backend/.../service/
- Create CareerRecommendationController: expose endpoints GET /api/v1/career-paths/{userId}/recommendations, POST /api/v1/career-paths/{userId}/goals
- Create SkillGapController: expose GET /api/v1/career-paths/{userId}/skill-gaps, POST /api/v1/skills/assessments
- Wire services into new controllers via constructor injection

For peopleanalytics:
- Has 9 services and 11 repositories but only 2 controllers
- Look at all service classes in peopleanalytics/backend/.../service/
- Create HeadcountController, TurnoverController, EngagementController as needed
- Each controller exposes the corresponding service's public methods as REST endpoints
- Follow the existing controller structure in the app for consistency

PR title: 'feat: expose service layer via controllers in careerpath and peopleanalytics'"

# D3
dispatch "$KEY2" "wave31-D3-arch-controllers" \
"$AUTONOMOUS_HEADER

TASK: Add missing controllers to experimentengine, telemetrycore, and localizationos.

For experimentengine (has 5 services but 0 controllers):
- Look at all service classes in experimentengine/backend/.../service/
- Create ExperimentController: POST /api/v1/experiments, GET /api/v1/experiments, GET /api/v1/experiments/{id}
- Create AssignmentController: POST /api/v1/experiments/{id}/assignments (assign user to variant), GET /api/v1/experiments/{id}/results
- Create VariantController: CRUD for experiment variants
- Wire services into controllers

For telemetrycore (has 7 services but 0 controllers):
- Create MetricsController: POST /api/v1/metrics/ingest (batch), GET /api/v1/metrics/query
- Create AlertController: CRUD for alert rules, GET /api/v1/alerts/active
- Wire existing services

For localizationos (has 1 service, 4 repos, 0 controllers):
- Create TranslationController: POST /api/v1/translations/translate, GET /api/v1/translations/{key}, GET /api/v1/translations?locale=en
- Create LocaleController: GET /api/v1/locales, POST /api/v1/locales

PR title: 'feat: add missing REST controllers to experimentengine, telemetrycore, localizationos'"

# D4
dispatch "$KEY2" "wave31-D4-arch-meetingbrain-mesh" \
"$AUTONOMOUS_HEADER

TASK: Expose additional endpoints in meetingbrain, integrationmesh, and datastoryteller.

For meetingbrain (has 7 repos, 3 services, only 1 controller):
- Current controller likely only covers basic CRUD
- Add MeetingAnalyticsController: GET /api/v1/meetings/stats (frequency, duration averages), GET /api/v1/meetings/{id}/action-items
- Add TranscriptController: POST /api/v1/meetings/{id}/transcripts (upload), GET /api/v1/meetings/{id}/summary
- Wire transcript summary to call LiteLLM (check if cc.ai.gateway-url is configured)

For integrationmesh (has 8 services, 1 controller):
- Add ConnectorController: CRUD for integration connectors (source → destination mappings)
- Add EventController: POST /api/v1/events/publish, GET /api/v1/events/{id}/status
- The 8 services contain the logic — just expose them

For datastoryteller (has 3 services, 8 repos):
- Add StoryGenerationController: POST /api/v1/stories/generate (takes dataset + context → calls LiteLLM → returns narrative)
- Add ChartNarrativeController: POST /api/v1/narratives/chart (takes chart data → LiteLLM → human-readable description)
- Wire to cc.ai.gateway-url

PR title: 'feat: expand API surface for meetingbrain, integrationmesh, datastoryteller'"

echo ""
echo "=== WAVE E: LiteLLM Wiring + Integration Tests ==="

# E1
dispatch "$KEY3" "wave31-E1-litellm" \
"$AUTONOMOUS_HEADER

TASK: Wire and test LiteLLM AI integration for: retirementplus, wealthplan, legalresearch, analyticsbuilder

For EACH app:
1. Check the app's service layer for classes that should call the AI gateway (look for comments like 'TODO: call AI' or services that build prompts but don't send them)
2. Verify cc.ai.gateway-url is in application.yml (add if missing with default http://localhost:4000)
3. If the app has an AiService/LlmService class, verify it makes a POST to \${cc.ai.gateway-url}/chat/completions with the correct OpenAI-compatible request body
4. Create src/test/resources/application-test.yml with:
   cc:
     ai:
       gateway-url: http://localhost:9999/mock-litellm
5. Add a test class named AiServiceIntegrationTest that:
   - Uses MockRestServiceServer or WireMock to stub POST /chat/completions → return a valid OpenAI response JSON
   - Calls the service method that uses AI
   - Asserts the response is correctly parsed and returned
   - Tests the error path: stub returns 503 → service throws a meaningful exception (not NPE)

For retirementplus specifically: the LiteLLMClient.java already exists - write tests FOR it.
For analyticsbuilder: look at application.yml - LiteLLM is configured, find where it should be called in AnalyticsService.

PR title: 'test(ai): LiteLLM integration tests for retirementplus, wealthplan, legalresearch, analyticsbuilder'"

# E2
dispatch "$KEY3" "wave31-E2-litellm" \
"$AUTONOMOUS_HEADER

TASK: Wire and test LiteLLM AI integration for: performancenarrative, cashflowanalyzer, churnpredictor, copyoptimizer

These apps all have cc.ai configured. Verify AI is actually wired and test it:

For performancenarrative: Find the service that generates performance narratives. It should call POST \${cc.ai.gateway-url}/chat/completions with a prompt built from the performance metrics. Add WireMock test.

For cashflowanalyzer: Has LiteLLM config and likely a cash flow insight generation service. Wire it if not done. Add test with mocked AI response returning cash flow analysis.

For churnpredictor: Already has AiOfferService with real AI calls. Add test using MockRestServiceServer that mocks the LiteLLM endpoint and verifies: (a) prompt contains customer data, (b) response is parsed to ChurnOffer object, (c) 429 rate limit error is retried once.

For copyoptimizer: AI content generation. Add test verifying: (a) user prompt is sanitized before sending to AI (no injection), (b) max token limit is enforced in request, (c) response content is returned to caller.

PR title: 'test(ai): LiteLLM integration tests for performancenarrative, cashflowanalyzer, churnpredictor, copyoptimizer'"

echo ""
echo "================================================================"
echo "Wave-31 dispatch complete. Session IDs saved to: $STATE"
echo "================================================================"
cat "$STATE"
