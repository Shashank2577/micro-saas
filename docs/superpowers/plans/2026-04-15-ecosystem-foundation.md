# Ecosystem Foundation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` (recommended) or `superpowers:executing-plans` to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the Nexus Hub orchestrator and app scaffold tooling that every other app in the ecosystem depends on.

**Architecture:** Nexus Hub is a Spring Boot 3.3.5 app using cc-starter for all cross-cutting concerns. It exposes an app registry (other apps register themselves), an event bus (cross-app webhook fan-out), and a workflow engine (if event X from app A, call capability Y on app B). A Next.js dashboard visualises the ecosystem. A shell script scaffolds new apps from the cc-template.

**Tech Stack:** Java 21, Spring Boot 3.3.5, cc-starter (Maven local), PostgreSQL 16 + pgvector, Redis 7, Keycloak 24, MinIO, LiteLLM, Next.js 14, Testcontainers, JUnit 5

---

## File Map

```
micro-saas/                                   ← this repo
├── infra/
│   ├── docker-compose.yml                    ← cc-platform for local dev
│   └── keycloak/realm-export.json            ← pre-configured Keycloak realm
├── nexus-hub/
│   ├── backend/
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/com/microsaas/nexushub/
│   │       │   ├── NexusHubApplication.java
│   │       │   ├── app/
│   │       │   │   ├── EcosystemApp.java                ← entity
│   │       │   │   ├── EcosystemAppRepository.java
│   │       │   │   ├── AppRegistrationRequest.java      ← DTO
│   │       │   │   ├── EcosystemAppService.java
│   │       │   │   └── EcosystemAppController.java
│   │       │   ├── event/
│   │       │   │   ├── EcosystemEvent.java              ← entity
│   │       │   │   ├── EcosystemEventRepository.java
│   │       │   │   ├── EventSubscription.java           ← entity
│   │       │   │   ├── EventSubscriptionRepository.java
│   │       │   │   ├── PublishEventRequest.java         ← DTO
│   │       │   │   ├── SubscribeRequest.java            ← DTO
│   │       │   │   ├── EventBusService.java
│   │       │   │   └── EventBusController.java
│   │       │   └── workflow/
│   │       │       ├── Workflow.java                    ← entity
│   │       │       ├── WorkflowRun.java                 ← entity
│   │       │       ├── WorkflowRepository.java
│   │       │       ├── WorkflowRunRepository.java
│   │       │       ├── CreateWorkflowRequest.java       ← DTO
│   │       │       ├── WorkflowExecutor.java            ← core logic
│   │       │       ├── WorkflowService.java
│   │       │       └── WorkflowController.java
│   │       ├── main/resources/
│   │       │   ├── db/migration/
│   │       │   │   ├── V1__create_ecosystem_apps.sql
│   │       │   │   ├── V2__create_ecosystem_events.sql
│   │       │   │   ├── V3__create_event_subscriptions.sql
│   │       │   │   ├── V4__create_workflows.sql
│   │       │   │   └── V5__create_workflow_runs.sql
│   │       │   └── application.yml
│   │       └── test/java/com/microsaas/nexushub/
│   │           ├── NexusHubTestBase.java                ← shared Testcontainers config
│   │           ├── app/EcosystemAppServiceTest.java
│   │           ├── event/EventBusServiceTest.java
│   │           └── workflow/WorkflowExecutorTest.java
│   └── frontend/
│       ├── src/
│       │   ├── app/
│       │   │   ├── page.tsx                             ← ecosystem dashboard
│       │   │   ├── apps/page.tsx                        ← registered apps list
│       │   │   ├── workflows/page.tsx                   ← workflow builder
│       │   │   └── events/page.tsx                      ← event stream
│       │   ├── components/
│       │   │   ├── AppCard.tsx
│       │   │   ├── EcosystemMap.tsx
│       │   │   └── WorkflowBuilder.tsx
│       │   └── lib/api.ts                               ← typed API client
│       ├── package.json
│       └── next.config.ts
└── tools/
    ├── scaffold-app.sh                                   ← bootstrap new app
    └── register-app.sh                                   ← register with Hub
```

---

## Task 1: Install cc-starter and scaffold the repo

**Files:**
- Create: `micro-saas/.gitignore`
- Create: `micro-saas/README.md`

- [ ] **Step 1: Install cc-starter into local Maven repository**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/cross-cutting
mvn install -DskipTests -q
```

Expected: `BUILD SUCCESS` — cc-starter is now available as `com.crosscutting:cc-starter:0.0.1-SNAPSHOT`

- [ ] **Step 2: Verify the install**

```bash
ls ~/.m2/repository/com/crosscutting/cc-starter/
```

Expected: `0.0.1-SNAPSHOT/` directory exists

- [ ] **Step 3: Initialise the micro-saas repo**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git init
```

Expected: `Initialized empty Git repository`

- [ ] **Step 4: Create .gitignore**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/.gitignore`:

```
# Java
target/
*.class
*.jar
*.war
.mvn/

# Node
node_modules/
.next/
.env*.local

# IntelliJ / VS Code
.idea/
*.iml
.vscode/

# macOS
.DS_Store

# Terraform
**/.terraform/
*.tfstate
*.tfstate.backup
*.tfvars

# Secrets
.env
.env.dev
.env.qa
.env.prod
secrets/
```

- [ ] **Step 5: Create README**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/README.md`:

```markdown
# Micro-SaaS AI Ecosystem

100 AI-native SaaS apps on a shared foundation.

## Structure

- `infra/` — local dev environment (Docker Compose)
- `nexus-hub/` — ecosystem orchestrator (first app to build)
- `tools/` — scaffold and register new apps
- `docs/` — specs and implementation plans

## Getting started

1. `cd infra && docker compose up -d`
2. `cd nexus-hub/backend && mvn spring-boot:run`
3. `cd nexus-hub/frontend && npm run dev`

See `docs/superpowers/plans/` for implementation plans.
```

- [ ] **Step 6: Commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add .gitignore README.md
git commit -m "chore: initialise micro-saas ecosystem repo"
```

---

## Task 2: Local dev environment (cc-platform)

**Files:**
- Create: `infra/docker-compose.yml`
- Create: `infra/keycloak/realm-export.json`
- Create: `infra/postgres/init.sql`

- [ ] **Step 1: Create infra directory**

```bash
mkdir -p /Users/shashanksaxena/Documents/Personal/Code/micro-saas/infra/keycloak
mkdir -p /Users/shashanksaxena/Documents/Personal/Code/micro-saas/infra/postgres
```

- [ ] **Step 2: Create docker-compose.yml**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/infra/docker-compose.yml`:

```yaml
services:
  postgres:
    image: pgvector/pgvector:pg16
    environment:
      POSTGRES_USER: cc
      POSTGRES_PASSWORD: cc
      POSTGRES_DB: cc
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./postgres/init.sql:/docker-entrypoint-initdb.d/init.sql

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

  keycloak:
    image: quay.io/keycloak/keycloak:24.0.3
    command: start-dev --import-realm
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
      KC_DB: postgres
      KC_DB_URL: jdbc:postgresql://postgres:5432/cc
      KC_DB_USERNAME: cc
      KC_DB_PASSWORD: cc
    ports:
      - "8180:8080"
    volumes:
      - ./keycloak:/opt/keycloak/data/import
    depends_on:
      - postgres

  minio:
    image: minio/minio:latest
    command: server /data --console-address ":9001"
    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin
    ports:
      - "9000:9000"
      - "9001:9001"
    volumes:
      - minio_data:/data

  litellm:
    image: ghcr.io/berriai/litellm:main-latest
    ports:
      - "4000:4000"
    environment:
      LITELLM_MASTER_KEY: "sk-local-dev-key"
    volumes:
      - ./litellm-config.yaml:/app/config.yaml
    command: --config /app/config.yaml --port 4000

volumes:
  postgres_data:
  minio_data:
```

- [ ] **Step 3: Create postgres init.sql**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/infra/postgres/init.sql`:

```sql
-- Create schemas used by cc-starter
CREATE SCHEMA IF NOT EXISTS cc;
CREATE SCHEMA IF NOT EXISTS tenant;
CREATE SCHEMA IF NOT EXISTS audit;

-- Enable pgvector
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

- [ ] **Step 4: Copy Keycloak realm from cross-cutting**

```bash
cp /Users/shashanksaxena/Documents/Personal/Code/cross-cutting/cc-platform/keycloak/realm-export.json \
   /Users/shashanksaxena/Documents/Personal/Code/micro-saas/infra/keycloak/realm-export.json
```

- [ ] **Step 5: Create LiteLLM config**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/infra/litellm-config.yaml`:

```yaml
model_list:
  - model_name: claude-sonnet-4-6
    litellm_params:
      model: anthropic/claude-sonnet-4-6
      api_key: os.environ/ANTHROPIC_API_KEY
  - model_name: gpt-4o
    litellm_params:
      model: openai/gpt-4o
      api_key: os.environ/OPENAI_API_KEY

general_settings:
  master_key: sk-local-dev-key
```

- [ ] **Step 6: Start the stack and verify**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/infra
docker compose up -d
```

Expected: All 5 containers start (postgres, redis, keycloak, minio, litellm)

```bash
docker compose ps
```

Expected: All services show `Up` status

- [ ] **Step 7: Verify Postgres schemas**

```bash
docker compose exec postgres psql -U cc -d cc -c "\dn"
```

Expected: `cc`, `tenant`, `audit` schemas listed

- [ ] **Step 8: Commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add infra/
git commit -m "chore: add cc-platform local dev environment"
```

---

## Task 3: Nexus Hub — Maven project skeleton

**Files:**
- Create: `nexus-hub/backend/pom.xml`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/NexusHubApplication.java`
- Create: `nexus-hub/backend/src/main/resources/application.yml`

- [ ] **Step 1: Create directory structure**

```bash
mkdir -p /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub
mkdir -p /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/resources/db/migration
mkdir -p /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/test/java/com/microsaas/nexushub
```

- [ ] **Step 2: Create pom.xml**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.5</version>
        <relativePath/>
    </parent>

    <groupId>com.microsaas</groupId>
    <artifactId>nexus-hub</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>nexus-hub</name>
    <description>Nexus Hub — Ecosystem Orchestrator</description>

    <properties>
        <java.version>21</java.version>
        <testcontainers.version>1.19.3</testcontainers.version>
    </properties>

    <dependencies>
        <!-- cc-starter: provides tenancy, auth, RBAC, audit, search, AI, webhooks, etc. -->
        <dependency>
            <groupId>com.crosscutting</groupId>
            <artifactId>cc-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <!-- Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- JPA + Postgres -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- JSONB support for Hibernate -->
        <dependency>
            <groupId>io.hypersistence</groupId>
            <artifactId>hypersistence-utils-hibernate-63</artifactId>
            <version>3.7.0</version>
        </dependency>

        <!-- Flyway -->
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-database-postgresql</artifactId>
        </dependency>

        <!-- Redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- OpenAPI docs -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.6.0</version>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${testcontainers.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <version>${testcontainers.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 3: Create application entry point**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/NexusHubApplication.java`:

```java
package com.microsaas.nexushub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NexusHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(NexusHubApplication.class, args);
    }
}
```

- [ ] **Step 4: Create application.yml**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: nexus-hub
  datasource:
    url: jdbc:postgresql://localhost:5432/cc
    username: cc
    password: cc
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        default_schema: tenant
  flyway:
    locations: classpath:db/migration
    default-schema: tenant
    schemas: tenant, cc, audit
  data:
    redis:
      host: localhost
      port: 6379

server:
  port: 8090

cc:
  tenancy:
    mode: header
    header-name: X-Tenant-ID
  auth:
    keycloak-url: http://localhost:8180
    realm: cc
    client-id: cc-backend
  ai:
    gateway-url: http://localhost:4000
    api-key: sk-local-dev-key
    default-model: claude-sonnet-4-6

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

- [ ] **Step 5: Verify Maven resolves dependencies**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn dependency:resolve -q
```

Expected: `BUILD SUCCESS` — all dependencies resolved including cc-starter

- [ ] **Step 6: Commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add nexus-hub/backend/pom.xml nexus-hub/backend/src/main/java nexus-hub/backend/src/main/resources/application.yml
git commit -m "feat(nexus-hub): maven skeleton with cc-starter dependency"
```

---

## Task 4: Database migrations (V1–V5)

**Files:**
- Create: `nexus-hub/backend/src/main/resources/db/migration/V1__create_ecosystem_apps.sql`
- Create: `nexus-hub/backend/src/main/resources/db/migration/V2__create_ecosystem_events.sql`
- Create: `nexus-hub/backend/src/main/resources/db/migration/V3__create_event_subscriptions.sql`
- Create: `nexus-hub/backend/src/main/resources/db/migration/V4__create_workflows.sql`
- Create: `nexus-hub/backend/src/main/resources/db/migration/V5__create_workflow_runs.sql`

- [ ] **Step 1: Create V1 — ecosystem_apps**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/resources/db/migration/V1__create_ecosystem_apps.sql`:

```sql
CREATE TABLE IF NOT EXISTS tenant.ecosystem_apps (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id    UUID         NOT NULL,
    name         VARCHAR(100) NOT NULL,
    display_name VARCHAR(200),
    base_url     VARCHAR(500),
    manifest     JSONB        NOT NULL DEFAULT '{}',
    status       VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',
    last_heartbeat_at TIMESTAMPTZ,
    registered_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_ecosystem_apps_tenant_name UNIQUE (tenant_id, name)
);

CREATE INDEX idx_ecosystem_apps_tenant ON tenant.ecosystem_apps (tenant_id);
CREATE INDEX idx_ecosystem_apps_status ON tenant.ecosystem_apps (status);
```

- [ ] **Step 2: Create V2 — ecosystem_events**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/resources/db/migration/V2__create_ecosystem_events.sql`:

```sql
CREATE TABLE IF NOT EXISTS tenant.ecosystem_events (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id  UUID         NOT NULL,
    source_app VARCHAR(100) NOT NULL,
    event_type VARCHAR(200) NOT NULL,
    payload    JSONB        NOT NULL DEFAULT '{}',
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_ecosystem_events_tenant_type ON tenant.ecosystem_events (tenant_id, event_type);
CREATE INDEX idx_ecosystem_events_created    ON tenant.ecosystem_events (created_at DESC);
```

- [ ] **Step 3: Create V3 — event_subscriptions**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/resources/db/migration/V3__create_event_subscriptions.sql`:

```sql
CREATE TABLE IF NOT EXISTS tenant.event_subscriptions (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id            UUID         NOT NULL,
    subscriber_app       VARCHAR(100) NOT NULL,
    event_type_pattern   VARCHAR(200) NOT NULL,
    callback_url         VARCHAR(500) NOT NULL,
    secret               VARCHAR(200),
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_subscription UNIQUE (tenant_id, subscriber_app, event_type_pattern)
);

CREATE INDEX idx_event_subscriptions_tenant ON tenant.event_subscriptions (tenant_id);
```

- [ ] **Step 4: Create V4 — workflows**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/resources/db/migration/V4__create_workflows.sql`:

```sql
CREATE TABLE IF NOT EXISTS tenant.workflows (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id         UUID         NOT NULL,
    name              VARCHAR(200) NOT NULL,
    description       TEXT,
    trigger_condition JSONB        NOT NULL DEFAULT '{}',
    steps             JSONB        NOT NULL DEFAULT '[]',
    enabled           BOOLEAN      NOT NULL DEFAULT TRUE,
    last_run_at       TIMESTAMPTZ,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_workflows_tenant   ON tenant.workflows (tenant_id);
CREATE INDEX idx_workflows_enabled  ON tenant.workflows (tenant_id, enabled);
```

- [ ] **Step 5: Create V5 — workflow_runs**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/resources/db/migration/V5__create_workflow_runs.sql`:

```sql
CREATE TABLE IF NOT EXISTS tenant.workflow_runs (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_id  UUID         NOT NULL REFERENCES tenant.workflows (id),
    triggered_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ,
    status       VARCHAR(50)  NOT NULL DEFAULT 'RUNNING',
    step_results JSONB,
    error_message TEXT
);

CREATE INDEX idx_workflow_runs_workflow ON tenant.workflow_runs (workflow_id);
CREATE INDEX idx_workflow_runs_status  ON tenant.workflow_runs (status);
```

- [ ] **Step 6: Apply migrations against local Postgres**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5432/cc \
    -Dflyway.user=cc -Dflyway.password=cc \
    -Dflyway.schemas=tenant -Dflyway.defaultSchema=tenant
```

Expected output contains: `Successfully applied 5 migrations`

- [ ] **Step 7: Verify tables exist**

```bash
docker compose -f /Users/shashanksaxena/Documents/Personal/Code/micro-saas/infra/docker-compose.yml \
    exec postgres psql -U cc -d cc -c "\dt tenant.*"
```

Expected: 5 tables listed: `ecosystem_apps`, `ecosystem_events`, `event_subscriptions`, `workflow_runs`, `workflows`

- [ ] **Step 8: Commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add nexus-hub/backend/src/main/resources/db/
git commit -m "feat(nexus-hub): database migrations V1-V5 (apps, events, subscriptions, workflows)"
```

---

## Task 5: App Registry

**Files:**
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/EcosystemApp.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/EcosystemAppRepository.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/AppRegistrationRequest.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/EcosystemAppService.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/EcosystemAppController.java`
- Create: `nexus-hub/backend/src/test/java/com/microsaas/nexushub/NexusHubTestBase.java`
- Create: `nexus-hub/backend/src/test/java/com/microsaas/nexushub/app/EcosystemAppServiceTest.java`

- [ ] **Step 1: Write the failing test**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/test/java/com/microsaas/nexushub/NexusHubTestBase.java`:

```java
package com.microsaas.nexushub;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class NexusHubTestBase {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("pgvector/pgvector:pg16")
                    .withDatabaseName("nexushub_test")
                    .withUsername("cc")
                    .withPassword("cc")
                    .withInitScript("db/test-init.sql");

    @Container
    @SuppressWarnings("resource")
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
        registry.add("spring.flyway.enabled", () -> "true");
    }
}
```

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/test/resources/db/test-init.sql`:

```sql
CREATE SCHEMA IF NOT EXISTS cc;
CREATE SCHEMA IF NOT EXISTS tenant;
CREATE SCHEMA IF NOT EXISTS audit;
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/test/java/com/microsaas/nexushub/app/EcosystemAppServiceTest.java`:

```java
package com.microsaas.nexushub.app;

import com.microsaas.nexushub.NexusHubTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class EcosystemAppServiceTest extends NexusHubTestBase {

    @Autowired
    private EcosystemAppService appService;

    @Autowired
    private EcosystemAppRepository appRepository;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        appRepository.deleteAll();
    }

    @Test
    void registerApp_persistsAppWithManifest() {
        AppRegistrationRequest request = new AppRegistrationRequest(
                "incidentbrain",
                "IncidentBrain",
                "https://api-incidentbrain.example.com",
                Map.of(
                        "emits", List.of("incident.opened", "incident.resolved"),
                        "consumes", List.of("deploy.completed"),
                        "capabilities", List.of()
                )
        );

        EcosystemApp app = appService.register(tenantId, request);

        assertThat(app.getId()).isNotNull();
        assertThat(app.getName()).isEqualTo("incidentbrain");
        assertThat(app.getTenantId()).isEqualTo(tenantId);
        assertThat(app.getStatus()).isEqualTo(EcosystemApp.AppStatus.ACTIVE);
        assertThat(app.getManifest()).containsKey("emits");
    }

    @Test
    void registerApp_duplicateName_updatesExisting() {
        AppRegistrationRequest first = new AppRegistrationRequest(
                "incidentbrain", "IncidentBrain", "https://v1.example.com", Map.of());
        AppRegistrationRequest second = new AppRegistrationRequest(
                "incidentbrain", "IncidentBrain v2", "https://v2.example.com", Map.of());

        appService.register(tenantId, first);
        EcosystemApp updated = appService.register(tenantId, second);

        assertThat(updated.getBaseUrl()).isEqualTo("https://v2.example.com");
        assertThat(appRepository.findByTenantId(tenantId)).hasSize(1);
    }

    @Test
    void listApps_returnsOnlyAppsForTenant() {
        UUID otherTenant = UUID.randomUUID();
        appService.register(tenantId,
                new AppRegistrationRequest("app-a", "App A", "https://a.example.com", Map.of()));
        appService.register(otherTenant,
                new AppRegistrationRequest("app-b", "App B", "https://b.example.com", Map.of()));

        List<EcosystemApp> apps = appService.listApps(tenantId);

        assertThat(apps).hasSize(1);
        assertThat(apps.get(0).getName()).isEqualTo("app-a");
    }

    @Test
    void deregisterApp_setsStatusInactive() {
        EcosystemApp app = appService.register(tenantId,
                new AppRegistrationRequest("app-a", "App A", "https://a.example.com", Map.of()));

        appService.deregister(tenantId, app.getId());

        EcosystemApp updated = appRepository.findById(app.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(EcosystemApp.AppStatus.INACTIVE);
    }
}
```

- [ ] **Step 2: Run tests — expect compilation failure**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn test -pl . -Dtest=EcosystemAppServiceTest -q 2>&1 | tail -20
```

Expected: `COMPILATION ERROR` — `EcosystemApp`, `EcosystemAppService`, `AppRegistrationRequest` do not exist yet

- [ ] **Step 3: Create EcosystemApp entity**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/EcosystemApp.java`:

```java
package com.microsaas.nexushub.app;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "ecosystem_apps", schema = "tenant")
@Data
@NoArgsConstructor
public class EcosystemApp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "base_url")
    private String baseUrl;

    @Type(JsonType.class)
    @Column(name = "manifest", columnDefinition = "jsonb")
    private Map<String, Object> manifest;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppStatus status = AppStatus.ACTIVE;

    @Column(name = "last_heartbeat_at")
    private Instant lastHeartbeatAt;

    @Column(name = "registered_at", nullable = false, updatable = false)
    private Instant registeredAt = Instant.now();

    public enum AppStatus {
        ACTIVE, INACTIVE, ERROR
    }
}
```

- [ ] **Step 4: Create AppRegistrationRequest DTO**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/AppRegistrationRequest.java`:

```java
package com.microsaas.nexushub.app;

import java.util.Map;

public record AppRegistrationRequest(
        String name,
        String displayName,
        String baseUrl,
        Map<String, Object> manifest
) {}
```

- [ ] **Step 5: Create EcosystemAppRepository**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/EcosystemAppRepository.java`:

```java
package com.microsaas.nexushub.app;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EcosystemAppRepository extends JpaRepository<EcosystemApp, UUID> {
    List<EcosystemApp> findByTenantId(UUID tenantId);
    Optional<EcosystemApp> findByTenantIdAndName(UUID tenantId, String name);
    List<EcosystemApp> findByTenantIdAndStatus(UUID tenantId, EcosystemApp.AppStatus status);
}
```

- [ ] **Step 6: Create EcosystemAppService**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/EcosystemAppService.java`:

```java
package com.microsaas.nexushub.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EcosystemAppService {

    private final EcosystemAppRepository appRepository;

    @Transactional
    public EcosystemApp register(UUID tenantId, AppRegistrationRequest request) {
        EcosystemApp app = appRepository.findByTenantIdAndName(tenantId, request.name())
                .orElseGet(EcosystemApp::new);

        app.setTenantId(tenantId);
        app.setName(request.name());
        app.setDisplayName(request.displayName());
        app.setBaseUrl(request.baseUrl());
        app.setManifest(request.manifest());
        app.setStatus(EcosystemApp.AppStatus.ACTIVE);
        app.setLastHeartbeatAt(Instant.now());

        return appRepository.save(app);
    }

    public List<EcosystemApp> listApps(UUID tenantId) {
        return appRepository.findByTenantId(tenantId);
    }

    public List<EcosystemApp> listActiveApps(UUID tenantId) {
        return appRepository.findByTenantIdAndStatus(tenantId, EcosystemApp.AppStatus.ACTIVE);
    }

    @Transactional
    public void deregister(UUID tenantId, UUID appId) {
        appRepository.findById(appId)
                .filter(a -> a.getTenantId().equals(tenantId))
                .ifPresent(a -> {
                    a.setStatus(EcosystemApp.AppStatus.INACTIVE);
                    appRepository.save(a);
                });
    }

    @Transactional
    public void heartbeat(UUID tenantId, String appName) {
        appRepository.findByTenantIdAndName(tenantId, appName)
                .ifPresent(a -> {
                    a.setLastHeartbeatAt(Instant.now());
                    appRepository.save(a);
                });
    }
}
```

- [ ] **Step 7: Create EcosystemAppController**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/app/EcosystemAppController.java`:

```java
package com.microsaas.nexushub.app;

import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/apps")
@RequiredArgsConstructor
public class EcosystemAppController {

    private final EcosystemAppService appService;

    @PostMapping("/register")
    public ResponseEntity<EcosystemApp> register(@RequestBody AppRegistrationRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(appService.register(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<EcosystemApp>> list() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(appService.listApps(tenantId));
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<Void> deregister(@PathVariable UUID appId) {
        UUID tenantId = TenantContext.require();
        appService.deregister(tenantId, appId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/heartbeat/{appName}")
    public ResponseEntity<Void> heartbeat(@PathVariable String appName) {
        UUID tenantId = TenantContext.require();
        appService.heartbeat(tenantId, appName);
        return ResponseEntity.ok().build();
    }
}
```

- [ ] **Step 8: Run tests — expect PASS**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn test -Dtest=EcosystemAppServiceTest -q
```

Expected: `Tests run: 4, Failures: 0, Errors: 0`

- [ ] **Step 9: Commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add nexus-hub/backend/src/
git commit -m "feat(nexus-hub): app registry — register, list, deregister, heartbeat"
```

---

## Task 6: Event Bus

**Files:**
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EcosystemEvent.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EcosystemEventRepository.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventSubscription.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventSubscriptionRepository.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/PublishEventRequest.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/SubscribeRequest.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventBusService.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventBusController.java`
- Create: `nexus-hub/backend/src/test/java/com/microsaas/nexushub/event/EventBusServiceTest.java`

- [ ] **Step 1: Write the failing test**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/test/java/com/microsaas/nexushub/event/EventBusServiceTest.java`:

```java
package com.microsaas.nexushub.event;

import com.microsaas.nexushub.NexusHubTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class EventBusServiceTest extends NexusHubTestBase {

    @Autowired
    private EventBusService eventBusService;

    @Autowired
    private EcosystemEventRepository eventRepository;

    @Autowired
    private EventSubscriptionRepository subscriptionRepository;

    @MockBean
    private RestTemplate restTemplate;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        subscriptionRepository.deleteAll();
    }

    @Test
    void publish_persistsEvent() {
        PublishEventRequest request = new PublishEventRequest(
                "incidentbrain",
                "incident.opened",
                Map.of("incidentId", "abc123", "severity", "P1")
        );

        EcosystemEvent event = eventBusService.publish(tenantId, request);

        assertThat(event.getId()).isNotNull();
        assertThat(event.getEventType()).isEqualTo("incident.opened");
        assertThat(event.getPayload()).containsKey("incidentId");
    }

    @Test
    void publish_notifiesMatchingSubscribers() {
        subscriptionRepository.save(buildSubscription(tenantId, "dealsignal", "incident.*",
                "https://dealsignal.example.com/webhooks"));
        subscriptionRepository.save(buildSubscription(tenantId, "obsvai", "deploy.*",
                "https://obsvai.example.com/webhooks"));

        eventBusService.publish(tenantId, new PublishEventRequest(
                "incidentbrain", "incident.resolved", Map.of("incidentId", "abc123")));

        // Only the incident.* subscriber should be called, not the deploy.* one
        verify(restTemplate, times(1)).postForEntity(
                eq("https://dealsignal.example.com/webhooks"), any(), any());
    }

    @Test
    void subscribe_registersSubscription() {
        SubscribeRequest request = new SubscribeRequest(
                "dealsignal", "incident.*", "https://dealsignal.example.com/webhooks", null);

        EventSubscription sub = eventBusService.subscribe(tenantId, request);

        assertThat(sub.getId()).isNotNull();
        assertThat(sub.getEventTypePattern()).isEqualTo("incident.*");
    }

    @Test
    void listEvents_returnsEventsForTenant() {
        UUID otherTenant = UUID.randomUUID();
        eventBusService.publish(tenantId,
                new PublishEventRequest("app-a", "test.event", Map.of()));
        eventBusService.publish(otherTenant,
                new PublishEventRequest("app-b", "test.event", Map.of()));

        List<EcosystemEvent> events = eventBusService.listEvents(tenantId, null, 50);

        assertThat(events).hasSize(1);
    }

    private EventSubscription buildSubscription(UUID tenantId, String app, String pattern, String url) {
        EventSubscription sub = new EventSubscription();
        sub.setTenantId(tenantId);
        sub.setSubscriberApp(app);
        sub.setEventTypePattern(pattern);
        sub.setCallbackUrl(url);
        return sub;
    }
}
```

- [ ] **Step 2: Run test — expect compilation failure**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn test -Dtest=EventBusServiceTest -q 2>&1 | tail -5
```

Expected: `COMPILATION ERROR`

- [ ] **Step 3: Create EcosystemEvent entity**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EcosystemEvent.java`:

```java
package com.microsaas.nexushub.event;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "ecosystem_events", schema = "tenant")
@Data
@NoArgsConstructor
public class EcosystemEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "source_app", nullable = false)
    private String sourceApp;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Type(JsonType.class)
    @Column(name = "payload", columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
```

- [ ] **Step 4: Create EventSubscription entity**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventSubscription.java`:

```java
package com.microsaas.nexushub.event;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event_subscriptions", schema = "tenant")
@Data
@NoArgsConstructor
public class EventSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "subscriber_app", nullable = false)
    private String subscriberApp;

    @Column(name = "event_type_pattern", nullable = false)
    private String eventTypePattern;

    @Column(name = "callback_url", nullable = false)
    private String callbackUrl;

    @Column(name = "secret")
    private String secret;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    /** Returns true if this subscription matches the given event type.
     *  Supports exact match and wildcard suffix: "incident.*" matches "incident.opened" */
    public boolean matches(String eventType) {
        if (!eventTypePattern.endsWith(".*")) {
            return eventTypePattern.equals(eventType);
        }
        String prefix = eventTypePattern.substring(0, eventTypePattern.length() - 2);
        return eventType.startsWith(prefix + ".");
    }
}
```

- [ ] **Step 5: Create repositories**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EcosystemEventRepository.java`:

```java
package com.microsaas.nexushub.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EcosystemEventRepository extends JpaRepository<EcosystemEvent, UUID> {
    List<EcosystemEvent> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);
    List<EcosystemEvent> findByTenantIdAndEventTypeOrderByCreatedAtDesc(UUID tenantId, String eventType);
}
```

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventSubscriptionRepository.java`:

```java
package com.microsaas.nexushub.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, UUID> {
    List<EventSubscription> findByTenantId(UUID tenantId);
}
```

- [ ] **Step 6: Create DTOs**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/PublishEventRequest.java`:

```java
package com.microsaas.nexushub.event;

import java.util.Map;

public record PublishEventRequest(
        String sourceApp,
        String eventType,
        Map<String, Object> payload
) {}
```

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/SubscribeRequest.java`:

```java
package com.microsaas.nexushub.event;

public record SubscribeRequest(
        String subscriberApp,
        String eventTypePattern,
        String callbackUrl,
        String secret
) {}
```

- [ ] **Step 7: Create EventBusService**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventBusService.java`:

```java
package com.microsaas.nexushub.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventBusService {

    private final EcosystemEventRepository eventRepository;
    private final EventSubscriptionRepository subscriptionRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public EcosystemEvent publish(UUID tenantId, PublishEventRequest request) {
        EcosystemEvent event = new EcosystemEvent();
        event.setTenantId(tenantId);
        event.setSourceApp(request.sourceApp());
        event.setEventType(request.eventType());
        event.setPayload(request.payload());
        EcosystemEvent saved = eventRepository.save(event);

        fanOutAsync(tenantId, saved);
        return saved;
    }

    @Async
    protected void fanOutAsync(UUID tenantId, EcosystemEvent event) {
        List<EventSubscription> subscribers = subscriptionRepository.findByTenantId(tenantId);
        subscribers.stream()
                .filter(s -> s.matches(event.getEventType()))
                .forEach(s -> deliverToSubscriber(s, event));
    }

    private void deliverToSubscriber(EventSubscription subscription, EcosystemEvent event) {
        try {
            restTemplate.postForEntity(subscription.getCallbackUrl(), event, Void.class);
        } catch (Exception e) {
            log.warn("Failed to deliver event {} to {}: {}",
                    event.getEventType(), subscription.getSubscriberApp(), e.getMessage());
        }
    }

    @Transactional
    public EventSubscription subscribe(UUID tenantId, SubscribeRequest request) {
        EventSubscription sub = new EventSubscription();
        sub.setTenantId(tenantId);
        sub.setSubscriberApp(request.subscriberApp());
        sub.setEventTypePattern(request.eventTypePattern());
        sub.setCallbackUrl(request.callbackUrl());
        sub.setSecret(request.secret());
        return subscriptionRepository.save(sub);
    }

    public List<EcosystemEvent> listEvents(UUID tenantId, String eventType, int limit) {
        List<EcosystemEvent> events = eventType != null
                ? eventRepository.findByTenantIdAndEventTypeOrderByCreatedAtDesc(tenantId, eventType)
                : eventRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        return events.stream().limit(limit).toList();
    }
}
```

- [ ] **Step 8: Register RestTemplate bean**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/config/NexusHubConfig.java`:

```java
package com.microsaas.nexushub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NexusHubConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

- [ ] **Step 9: Create EventBusController**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventBusController.java`:

```java
package com.microsaas.nexushub.event;

import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventBusController {

    private final EventBusService eventBusService;

    @PostMapping
    public ResponseEntity<EcosystemEvent> publish(@RequestBody PublishEventRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(eventBusService.publish(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<EcosystemEvent>> list(
            @RequestParam(required = false) String eventType,
            @RequestParam(defaultValue = "100") int limit) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(eventBusService.listEvents(tenantId, eventType, limit));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<EventSubscription> subscribe(@RequestBody SubscribeRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(eventBusService.subscribe(tenantId, request));
    }
}
```

- [ ] **Step 10: Run tests — expect PASS**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn test -Dtest=EventBusServiceTest -q
```

Expected: `Tests run: 4, Failures: 0, Errors: 0`

- [ ] **Step 11: Commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add nexus-hub/backend/src/
git commit -m "feat(nexus-hub): event bus — publish, subscribe, fan-out with wildcard patterns"
```

---

## Task 7: Workflow Engine

**Files:**
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/Workflow.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowRun.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowRepository.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowRunRepository.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/CreateWorkflowRequest.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowExecutor.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowService.java`
- Create: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowController.java`
- Create: `nexus-hub/backend/src/test/java/com/microsaas/nexushub/workflow/WorkflowExecutorTest.java`

- [ ] **Step 1: Write the failing test**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/test/java/com/microsaas/nexushub/workflow/WorkflowExecutorTest.java`:

```java
package com.microsaas.nexushub.workflow;

import com.microsaas.nexushub.NexusHubTestBase;
import com.microsaas.nexushub.event.EcosystemEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WorkflowExecutorTest extends NexusHubTestBase {

    @Autowired
    private WorkflowExecutor executor;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowRunRepository runRepository;

    @MockBean
    private RestTemplate restTemplate;

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void triggerCondition_matchesEventType() {
        Workflow workflow = buildWorkflow(tenantId,
                Map.of("type", "event", "eventType", "incident.opened"),
                List.of()
        );
        workflowRepository.save(workflow);

        EcosystemEvent event = buildEvent(tenantId, "incident.opened");
        List<Workflow> matched = executor.findMatchingWorkflows(tenantId, event);

        assertThat(matched).hasSize(1);
    }

    @Test
    void triggerCondition_doesNotMatchWrongEventType() {
        Workflow workflow = buildWorkflow(tenantId,
                Map.of("type", "event", "eventType", "incident.opened"),
                List.of()
        );
        workflowRepository.save(workflow);

        EcosystemEvent event = buildEvent(tenantId, "deal.updated");
        List<Workflow> matched = executor.findMatchingWorkflows(tenantId, event);

        assertThat(matched).isEmpty();
    }

    @Test
    void execute_createsWorkflowRun_withCompletedStatus() {
        Workflow workflow = buildWorkflow(tenantId,
                Map.of("type", "event", "eventType", "incident.opened"),
                List.of()
        );
        workflowRepository.save(workflow);

        EcosystemEvent event = buildEvent(tenantId, "incident.opened");
        WorkflowRun run = executor.execute(workflow, event);

        assertThat(run.getId()).isNotNull();
        assertThat(run.getStatus()).isEqualTo(WorkflowRun.RunStatus.COMPLETED);
    }

    @Test
    void disabledWorkflow_isNotTriggered() {
        Workflow workflow = buildWorkflow(tenantId,
                Map.of("type", "event", "eventType", "incident.opened"),
                List.of()
        );
        workflow.setEnabled(false);
        workflowRepository.save(workflow);

        EcosystemEvent event = buildEvent(tenantId, "incident.opened");
        List<Workflow> matched = executor.findMatchingWorkflows(tenantId, event);

        assertThat(matched).isEmpty();
    }

    private Workflow buildWorkflow(UUID tenantId, Map<String, Object> trigger, List<Map<String, Object>> steps) {
        Workflow w = new Workflow();
        w.setTenantId(tenantId);
        w.setName("Test Workflow");
        w.setTriggerCondition(trigger);
        w.setSteps(steps);
        w.setEnabled(true);
        return w;
    }

    private EcosystemEvent buildEvent(UUID tenantId, String eventType) {
        EcosystemEvent e = new EcosystemEvent();
        e.setTenantId(tenantId);
        e.setSourceApp("test-app");
        e.setEventType(eventType);
        e.setPayload(Map.of());
        return e;
    }
}
```

- [ ] **Step 2: Run test — expect compilation failure**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn test -Dtest=WorkflowExecutorTest -q 2>&1 | tail -5
```

Expected: `COMPILATION ERROR`

- [ ] **Step 3: Create Workflow entity**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/Workflow.java`:

```java
package com.microsaas.nexushub.workflow;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "workflows", schema = "tenant")
@Data
@NoArgsConstructor
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Type(JsonType.class)
    @Column(name = "trigger_condition", columnDefinition = "jsonb")
    private Map<String, Object> triggerCondition;

    @Type(JsonType.class)
    @Column(name = "steps", columnDefinition = "jsonb")
    private List<Map<String, Object>> steps;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "last_run_at")
    private Instant lastRunAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();
}
```

- [ ] **Step 4: Create WorkflowRun entity**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowRun.java`:

```java
package com.microsaas.nexushub.workflow;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "workflow_runs", schema = "tenant")
@Data
@NoArgsConstructor
public class WorkflowRun {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Column(name = "triggered_at", nullable = false, updatable = false)
    private Instant triggeredAt = Instant.now();

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RunStatus status = RunStatus.RUNNING;

    @Type(JsonType.class)
    @Column(name = "step_results", columnDefinition = "jsonb")
    private Map<String, Object> stepResults;

    @Column(name = "error_message")
    private String errorMessage;

    public enum RunStatus {
        RUNNING, COMPLETED, FAILED
    }
}
```

- [ ] **Step 5: Create repositories**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowRepository.java`:

```java
package com.microsaas.nexushub.workflow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowRepository extends JpaRepository<Workflow, UUID> {
    List<Workflow> findByTenantIdAndEnabled(UUID tenantId, boolean enabled);
}
```

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowRunRepository.java`:

```java
package com.microsaas.nexushub.workflow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowRunRepository extends JpaRepository<WorkflowRun, UUID> {
    List<WorkflowRun> findByWorkflowIdOrderByTriggeredAtDesc(UUID workflowId);
}
```

- [ ] **Step 6: Create WorkflowExecutor**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowExecutor.java`:

```java
package com.microsaas.nexushub.workflow;

import com.microsaas.nexushub.event.EcosystemEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkflowExecutor {

    private final WorkflowRepository workflowRepository;
    private final WorkflowRunRepository runRepository;

    public List<Workflow> findMatchingWorkflows(UUID tenantId, EcosystemEvent event) {
        return workflowRepository.findByTenantIdAndEnabled(tenantId, true)
                .stream()
                .filter(w -> matchesTrigger(w.getTriggerCondition(), event))
                .toList();
    }

    @Transactional
    public WorkflowRun execute(Workflow workflow, EcosystemEvent triggerEvent) {
        WorkflowRun run = new WorkflowRun();
        run.setWorkflow(workflow);
        run.setStatus(WorkflowRun.RunStatus.RUNNING);
        runRepository.save(run);

        try {
            Map<String, Object> results = executeSteps(workflow.getSteps(), triggerEvent);
            run.setStepResults(results);
            run.setStatus(WorkflowRun.RunStatus.COMPLETED);
            run.setCompletedAt(Instant.now());
        } catch (Exception e) {
            log.error("Workflow {} failed: {}", workflow.getId(), e.getMessage(), e);
            run.setStatus(WorkflowRun.RunStatus.FAILED);
            run.setErrorMessage(e.getMessage());
            run.setCompletedAt(Instant.now());
        }

        workflow.setLastRunAt(Instant.now());
        workflowRepository.save(workflow);
        return runRepository.save(run);
    }

    private boolean matchesTrigger(Map<String, Object> trigger, EcosystemEvent event) {
        if (!"event".equals(trigger.get("type"))) {
            return false;
        }
        String requiredEventType = (String) trigger.get("eventType");
        if (requiredEventType == null) return false;

        if (requiredEventType.endsWith(".*")) {
            String prefix = requiredEventType.substring(0, requiredEventType.length() - 2);
            return event.getEventType().startsWith(prefix + ".");
        }
        return requiredEventType.equals(event.getEventType());
    }

    private Map<String, Object> executeSteps(List<Map<String, Object>> steps, EcosystemEvent triggerEvent) {
        // Step execution is intentionally simple in v1.
        // Each step is logged; capability calls will be added in Cluster I (IntegrationMesh).
        steps.forEach(step -> log.info("Executing step type={} for trigger event={}",
                step.get("type"), triggerEvent.getEventType()));
        return Map.of("stepsExecuted", steps.size(), "triggeredBy", triggerEvent.getEventType());
    }
}
```

- [ ] **Step 7: Create WorkflowService and Controller**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/CreateWorkflowRequest.java`:

```java
package com.microsaas.nexushub.workflow;

import java.util.List;
import java.util.Map;

public record CreateWorkflowRequest(
        String name,
        String description,
        Map<String, Object> triggerCondition,
        List<Map<String, Object>> steps
) {}
```

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowService.java`:

```java
package com.microsaas.nexushub.workflow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    @Transactional
    public Workflow create(UUID tenantId, CreateWorkflowRequest request) {
        Workflow workflow = new Workflow();
        workflow.setTenantId(tenantId);
        workflow.setName(request.name());
        workflow.setDescription(request.description());
        workflow.setTriggerCondition(request.triggerCondition());
        workflow.setSteps(request.steps());
        workflow.setEnabled(true);
        return workflowRepository.save(workflow);
    }

    public List<Workflow> listWorkflows(UUID tenantId) {
        return workflowRepository.findByTenantIdAndEnabled(tenantId, true);
    }

    @Transactional
    public void toggleEnabled(UUID tenantId, UUID workflowId, boolean enabled) {
        workflowRepository.findById(workflowId)
                .filter(w -> w.getTenantId().equals(tenantId))
                .ifPresent(w -> {
                    w.setEnabled(enabled);
                    w.setUpdatedAt(Instant.now());
                    workflowRepository.save(w);
                });
    }
}
```

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend/src/main/java/com/microsaas/nexushub/workflow/WorkflowController.java`:

```java
package com.microsaas.nexushub.workflow;

import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @PostMapping
    public ResponseEntity<Workflow> create(@RequestBody CreateWorkflowRequest request) {
        return ResponseEntity.ok(workflowService.create(TenantContext.require(), request));
    }

    @GetMapping
    public ResponseEntity<List<Workflow>> list() {
        return ResponseEntity.ok(workflowService.listWorkflows(TenantContext.require()));
    }

    @PatchMapping("/{workflowId}/enabled")
    public ResponseEntity<Void> toggleEnabled(@PathVariable UUID workflowId,
                                              @RequestParam boolean enabled) {
        workflowService.toggleEnabled(TenantContext.require(), workflowId, enabled);
        return ResponseEntity.ok().build();
    }
}
```

- [ ] **Step 8: Run all tests — expect PASS**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn test -q
```

Expected: All test classes pass. `BUILD SUCCESS`

- [ ] **Step 9: Commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add nexus-hub/backend/src/
git commit -m "feat(nexus-hub): workflow engine — create, trigger on events, execute steps"
```

---

## Task 8: Wire event bus to workflow engine + smoke test

**Files:**
- Modify: `nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventBusService.java`

- [ ] **Step 1: Inject WorkflowExecutor into EventBusService and trigger matching workflows**

Edit `EventBusService.java` — update the class declaration and `fanOutAsync` method:

```java
// Add to imports:
import com.microsaas.nexushub.workflow.Workflow;
import com.microsaas.nexushub.workflow.WorkflowExecutor;

// Add field (alongside existing fields):
private final WorkflowExecutor workflowExecutor;

// Update fanOutAsync:
@Async
protected void fanOutAsync(UUID tenantId, EcosystemEvent event) {
    // Fan out to webhook subscribers
    List<EventSubscription> subscribers = subscriptionRepository.findByTenantId(tenantId);
    subscribers.stream()
            .filter(s -> s.matches(event.getEventType()))
            .forEach(s -> deliverToSubscriber(s, event));

    // Trigger matching workflows
    List<Workflow> workflows = workflowExecutor.findMatchingWorkflows(tenantId, event);
    workflows.forEach(w -> workflowExecutor.execute(w, event));
}
```

- [ ] **Step 2: Run full test suite to confirm no regressions**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn test -q
```

Expected: `BUILD SUCCESS`

- [ ] **Step 3: Boot the application against local infra**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn spring-boot:run &
sleep 15
```

- [ ] **Step 4: Smoke test app registration**

```bash
curl -s -X POST http://localhost:8090/api/v1/apps/register \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: 00000000-0000-0000-0000-000000000001" \
  -d '{
    "name": "incidentbrain",
    "displayName": "IncidentBrain",
    "baseUrl": "https://api-incidentbrain.example.com",
    "manifest": {
      "emits": ["incident.opened","incident.resolved"],
      "consumes": ["deploy.completed"],
      "capabilities": []
    }
  }' | python3 -m json.tool
```

Expected: JSON response with `id` field and `"status": "ACTIVE"`

- [ ] **Step 5: Smoke test event publish**

```bash
curl -s -X POST http://localhost:8090/api/v1/events \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: 00000000-0000-0000-0000-000000000001" \
  -d '{
    "sourceApp": "incidentbrain",
    "eventType": "incident.opened",
    "payload": {"incidentId": "abc123", "severity": "P1"}
  }' | python3 -m json.tool
```

Expected: JSON response with `id` and `eventType: "incident.opened"`

- [ ] **Step 6: Stop the app and commit**

```bash
pkill -f "spring-boot:run" 2>/dev/null; true
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add nexus-hub/backend/src/main/java/com/microsaas/nexushub/event/EventBusService.java
git commit -m "feat(nexus-hub): wire event bus to workflow executor — events trigger workflows"
```

---

## Task 9: Nexus Hub frontend dashboard

**Files:**
- Create: `nexus-hub/frontend/package.json`
- Create: `nexus-hub/frontend/next.config.ts`
- Create: `nexus-hub/frontend/src/app/page.tsx`
- Create: `nexus-hub/frontend/src/app/apps/page.tsx`
- Create: `nexus-hub/frontend/src/components/AppCard.tsx`
- Create: `nexus-hub/frontend/src/lib/api.ts`

- [ ] **Step 1: Scaffold Next.js project**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub
npx create-next-app@14 frontend --typescript --tailwind --app --no-src-dir --no-git --import-alias "@/*" 2>/dev/null
```

Expected: `Success! Created frontend`

- [ ] **Step 2: Install cc-client React SDK and UI deps**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/frontend
npm install @crosscutting/react lucide-react 2>/dev/null
```

- [ ] **Step 3: Create typed API client**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/frontend/src/lib/api.ts`:

```typescript
const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8090';
const TENANT_ID = process.env.NEXT_PUBLIC_TENANT_ID ?? '00000000-0000-0000-0000-000000000001';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': TENANT_ID,
};

export interface EcosystemApp {
  id: string;
  name: string;
  displayName: string;
  baseUrl: string;
  status: 'ACTIVE' | 'INACTIVE' | 'ERROR';
  lastHeartbeatAt: string | null;
  registeredAt: string;
  manifest: Record<string, unknown>;
}

export interface EcosystemEvent {
  id: string;
  sourceApp: string;
  eventType: string;
  payload: Record<string, unknown>;
  createdAt: string;
}

export interface Workflow {
  id: string;
  name: string;
  enabled: boolean;
  lastRunAt: string | null;
  triggerCondition: Record<string, unknown>;
  steps: Record<string, unknown>[];
}

export const api = {
  apps: {
    list: async (): Promise<EcosystemApp[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/apps`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch apps: ${res.status}`);
      return res.json();
    },
    register: async (data: Omit<EcosystemApp, 'id' | 'status' | 'lastHeartbeatAt' | 'registeredAt'>): Promise<EcosystemApp> => {
      const res = await fetch(`${BASE_URL}/api/v1/apps/register`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to register app: ${res.status}`);
      return res.json();
    },
  },
  events: {
    list: async (limit = 50): Promise<EcosystemEvent[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/events?limit=${limit}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch events: ${res.status}`);
      return res.json();
    },
  },
  workflows: {
    list: async (): Promise<Workflow[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/workflows`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch workflows: ${res.status}`);
      return res.json();
    },
  },
};
```

- [ ] **Step 4: Create AppCard component**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/frontend/src/components/AppCard.tsx`:

```tsx
import { EcosystemApp } from '@/lib/api';

interface Props {
  app: EcosystemApp;
}

export function AppCard({ app }: Props) {
  const statusColor: Record<string, string> = {
    ACTIVE: 'bg-green-100 text-green-800',
    INACTIVE: 'bg-gray-100 text-gray-600',
    ERROR: 'bg-red-100 text-red-800',
  };

  return (
    <div className="border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow">
      <div className="flex items-start justify-between mb-2">
        <div>
          <h3 className="font-semibold text-gray-900">{app.displayName}</h3>
          <p className="text-sm text-gray-500">{app.name}</p>
        </div>
        <span className={`text-xs font-medium px-2 py-1 rounded-full ${statusColor[app.status]}`}>
          {app.status}
        </span>
      </div>
      <p className="text-xs text-gray-400 truncate">{app.baseUrl}</p>
      {app.lastHeartbeatAt && (
        <p className="text-xs text-gray-400 mt-1">
          Last seen: {new Date(app.lastHeartbeatAt).toLocaleString()}
        </p>
      )}
    </div>
  );
}
```

- [ ] **Step 5: Create ecosystem dashboard page**

Replace `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/frontend/src/app/page.tsx`:

```tsx
import { api } from '@/lib/api';
import { AppCard } from '@/components/AppCard';

export default async function DashboardPage() {
  let apps = [];
  let events = [];
  let workflows = [];

  try {
    [apps, events, workflows] = await Promise.all([
      api.apps.list(),
      api.events.list(10),
      api.workflows.list(),
    ]);
  } catch {
    // Backend may not be running in build/static environments
  }

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Nexus Hub</h1>
          <p className="text-gray-500 mt-1">Ecosystem orchestrator — {apps.length} apps connected</p>
        </div>

        {/* Stats row */}
        <div className="grid grid-cols-3 gap-4 mb-8">
          {[
            { label: 'Connected Apps', value: apps.length },
            { label: 'Active Workflows', value: workflows.filter((w) => w.enabled).length },
            { label: 'Events (last 10)', value: events.length },
          ].map((stat) => (
            <div key={stat.label} className="bg-white rounded-lg border border-gray-200 p-4">
              <p className="text-sm text-gray-500">{stat.label}</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">{stat.value}</p>
            </div>
          ))}
        </div>

        {/* Apps grid */}
        <section className="mb-8">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Connected Apps</h2>
          {apps.length === 0 ? (
            <p className="text-gray-400 text-sm">No apps registered yet. Use the API to register your first app.</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
              {apps.map((app) => <AppCard key={app.id} app={app} />)}
            </div>
          )}
        </section>

        {/* Recent events */}
        <section>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Recent Events</h2>
          {events.length === 0 ? (
            <p className="text-gray-400 text-sm">No events yet.</p>
          ) : (
            <div className="bg-white border border-gray-200 rounded-lg divide-y divide-gray-100">
              {events.map((event) => (
                <div key={event.id} className="px-4 py-3 flex items-center gap-4">
                  <span className="text-xs font-mono bg-blue-50 text-blue-700 px-2 py-0.5 rounded">
                    {event.eventType}
                  </span>
                  <span className="text-sm text-gray-500">from {event.sourceApp}</span>
                  <span className="text-xs text-gray-400 ml-auto">
                    {new Date(event.createdAt).toLocaleTimeString()}
                  </span>
                </div>
              ))}
            </div>
          )}
        </section>
      </div>
    </main>
  );
}
```

- [ ] **Step 6: Verify frontend builds**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/frontend
npm run build 2>&1 | tail -10
```

Expected: `✓ Compiled successfully` (or `Route (app) Size` table)

- [ ] **Step 7: Commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add nexus-hub/frontend/
git commit -m "feat(nexus-hub): Next.js dashboard — apps, events, workflows"
```

---

## Task 10: App scaffold generator

**Files:**
- Create: `tools/scaffold-app.sh`
- Create: `tools/register-app.sh`

- [ ] **Step 1: Create directory**

```bash
mkdir -p /Users/shashanksaxena/Documents/Personal/Code/micro-saas/tools
```

- [ ] **Step 2: Create scaffold-app.sh**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/tools/scaffold-app.sh`:

```bash
#!/usr/bin/env bash
set -euo pipefail

# scaffold-app.sh — Bootstrap a new app in the micro-saas ecosystem
# Usage: ./tools/scaffold-app.sh <app-name> <display-name> <port>
# Example: ./tools/scaffold-app.sh incidentbrain "IncidentBrain" 8091

APP_NAME="${1:?Usage: scaffold-app.sh <app-name> <display-name> <port>}"
DISPLAY_NAME="${2:?Provide a display name}"
PORT="${3:?Provide a backend port (e.g. 8091)}"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CC_TEMPLATE="${HOME}/Documents/Personal/Code/cross-cutting/cc-template"
TARGET="${REPO_ROOT}/${APP_NAME}"
JAVA_PACKAGE="com.microsaas.${APP_NAME//-/}"

echo "Scaffolding app: ${DISPLAY_NAME} (${APP_NAME}) on port ${PORT}"

# 1. Copy cc-template as the base
if [ ! -d "${CC_TEMPLATE}" ]; then
  echo "ERROR: cc-template not found at ${CC_TEMPLATE}"
  exit 1
fi

cp -r "${CC_TEMPLATE}" "${TARGET}"

# 2. Replace placeholders in all files
find "${TARGET}" -type f \( -name "*.java" -o -name "*.xml" -o -name "*.yml" -o -name "*.ts" -o -name "*.json" \) | while read -r file; do
  sed -i '' \
    -e "s/cc-template/${APP_NAME}/g" \
    -e "s/CcTemplate/${DISPLAY_NAME//[^a-zA-Z]/}/g" \
    -e "s/com\.crosscutting\.template/${JAVA_PACKAGE}/g" \
    -e "s/8080/${PORT}/g" \
    "${file}"
done

# 3. Rename Java package directories
OLD_PKG_DIR="${TARGET}/backend/src/main/java/com/crosscutting/template"
NEW_PKG_DIR="${TARGET}/backend/src/main/java/${JAVA_PACKAGE//./\/}"
if [ -d "${OLD_PKG_DIR}" ]; then
  mkdir -p "$(dirname "${NEW_PKG_DIR}")"
  mv "${OLD_PKG_DIR}" "${NEW_PKG_DIR}"
fi

# 4. Create integration manifest file
cat > "${TARGET}/integration-manifest.json" <<EOF
{
  "app": "${APP_NAME}",
  "displayName": "${DISPLAY_NAME}",
  "version": "0.0.1",
  "baseUrl": "https://api-${APP_NAME}.YOURDOMAIN.com",
  "emits": [],
  "consumes": [],
  "capabilities": [],
  "healthEndpoint": "/actuator/health"
}
EOF

# 5. Emit next steps
echo ""
echo "✓ App scaffolded at: ${TARGET}"
echo ""
echo "Next steps:"
echo "  1. cd ${TARGET}/backend && mvn spring-boot:run"
echo "  2. Edit integration-manifest.json to declare emits/consumes/capabilities"
echo "  3. Run ./tools/register-app.sh ${APP_NAME} to register with Nexus Hub"
echo "  4. Run ./freestack-init.sh to provision infrastructure"
```

- [ ] **Step 3: Create register-app.sh**

Create `/Users/shashanksaxena/Documents/Personal/Code/micro-saas/tools/register-app.sh`:

```bash
#!/usr/bin/env bash
set -euo pipefail

# register-app.sh — Register an app with Nexus Hub
# Usage: ./tools/register-app.sh <app-name>
# Reads integration-manifest.json from <app-name>/integration-manifest.json

APP_NAME="${1:?Usage: register-app.sh <app-name>}"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MANIFEST_FILE="${REPO_ROOT}/${APP_NAME}/integration-manifest.json"
HUB_URL="${HUB_URL:-http://localhost:8090}"
TENANT_ID="${TENANT_ID:-00000000-0000-0000-0000-000000000001}"

if [ ! -f "${MANIFEST_FILE}" ]; then
  echo "ERROR: integration-manifest.json not found at ${MANIFEST_FILE}"
  exit 1
fi

echo "Registering ${APP_NAME} with Nexus Hub at ${HUB_URL}..."

DISPLAY_NAME=$(jq -r '.displayName' "${MANIFEST_FILE}")
BASE_URL=$(jq -r '.baseUrl' "${MANIFEST_FILE}")
MANIFEST=$(cat "${MANIFEST_FILE}")

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "${HUB_URL}/api/v1/apps/register" \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: ${TENANT_ID}" \
  -d "{\"name\": \"${APP_NAME}\", \"displayName\": \"${DISPLAY_NAME}\", \"baseUrl\": \"${BASE_URL}\", \"manifest\": ${MANIFEST}}")

HTTP_CODE=$(echo "${RESPONSE}" | tail -1)
BODY=$(echo "${RESPONSE}" | head -1)

if [ "${HTTP_CODE}" == "200" ]; then
  echo "✓ Registered successfully. App ID: $(echo "${BODY}" | jq -r '.id')"
else
  echo "ERROR: Registration failed with HTTP ${HTTP_CODE}"
  echo "${BODY}"
  exit 1
fi
```

- [ ] **Step 4: Make scripts executable**

```bash
chmod +x /Users/shashanksaxena/Documents/Personal/Code/micro-saas/tools/scaffold-app.sh
chmod +x /Users/shashanksaxena/Documents/Personal/Code/micro-saas/tools/register-app.sh
```

- [ ] **Step 5: Test scaffold-app.sh (dry run)**

```bash
bash -n /Users/shashanksaxena/Documents/Personal/Code/micro-saas/tools/scaffold-app.sh
echo "Syntax OK"
```

Expected: `Syntax OK`

- [ ] **Step 6: Run final full test suite**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas/nexus-hub/backend
mvn test -q
```

Expected: All tests pass, `BUILD SUCCESS`

- [ ] **Step 7: Final commit**

```bash
cd /Users/shashanksaxena/Documents/Personal/Code/micro-saas
git add tools/
git commit -m "feat(tools): scaffold-app.sh and register-app.sh — bootstrap and register new ecosystem apps"
```

---

## Self-Review

**Spec coverage check:**
- [x] Hub-and-spoke architecture → Nexus Hub built in Tasks 3–9
- [x] App registry (register, list, deregister, heartbeat) → Task 5
- [x] Event bus with wildcard subscriptions → Task 6
- [x] Workflow engine (event triggers → execute steps) → Task 7
- [x] Event bus wired to workflow engine → Task 8
- [x] Frontend dashboard → Task 9
- [x] App scaffold generator → Task 10
- [x] Local dev environment → Task 2
- [x] cc-starter installed → Task 1
- [x] Integration manifest format defined → Task 10

**Placeholder scan:** No TBD, TODO, or "similar to Task N" patterns. All code blocks are complete.

**Type consistency:**
- `EcosystemApp.AppStatus` used consistently in entity, repository, and service
- `TenantContext.require()` used consistently in all controllers
- `WorkflowRun.RunStatus` used consistently in entity and executor
- `EventSubscription.matches()` used consistently in `EventBusService.fanOutAsync()`

---

## Execution Handoff

Plan complete and saved to `docs/superpowers/plans/2026-04-15-ecosystem-foundation.md`.

**Two execution options:**

**1. Subagent-Driven (recommended)** — Fresh subagent per task, reviewed between tasks, fast iteration. Invoke `superpowers:subagent-driven-development`.

**2. Inline Execution** — Execute tasks in this session. Invoke `superpowers:executing-plans`.

Which approach?
