# cc-starter -- Cross-Cutting SaaS Starter

## Overview

`cc-starter` is a Spring Boot starter library that provides 16 cross-cutting SaaS modules -- multi-tenancy, auth, RBAC, audit logging, feature flags, notifications, webhooks, background jobs, full-text and vector search, file storage, async export, security hardening, AI gateway, payments, structured logging, and error handling -- as a single Maven dependency. Add it to any Spring Boot 3.3.5 / Java 21 application, point it at PostgreSQL, Redis, and Keycloak, and you get a production-ready SaaS backend without writing any infrastructure code.

---

## Quick Start

### 1. Maven Dependency

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.5</version>
    <relativePath/>
</parent>

<properties>
    <java.version>21</java.version>
</properties>

<dependencies>
    <dependency>
        <groupId>com.crosscutting</groupId>
        <artifactId>cc-starter</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

Install to your local Maven repo first (not yet on Maven Central):

```bash
cd cross-cutting
mvn clean install -pl cc-starter -DskipTests
```

### 2. Minimum `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/crosscutting
    username: cc
    password: cc_dev
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/cc-platform
  data:
    redis:
      host: localhost
      port: 6379

server:
  port: 8080
```

Everything else has sensible defaults via `cc-defaults.yml` bundled in the JAR.

### 3. Start Infrastructure (cc-platform)

```bash
cd cc-platform
docker compose up -d
```

This starts PostgreSQL 16 (pgvector), Redis 7, Keycloak 24, MinIO, and LiteLLM. Wait for all services to be healthy:

```bash
docker compose ps
```

### 4. Run Your Application

```bash
mvn spring-boot:run
```

Flyway automatically runs all platform migrations. Verify at `http://localhost:8080/actuator/health`.

### 5. Get a JWT Token

```bash
TOKEN=$(curl -s -X POST http://localhost:8180/realms/cc-platform/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=cc-api" \
  -d "client_secret=cc-api-secret" \
  -d "grant_type=password" \
  -d "username=testuser" \
  -d "password=test1234" | jq -r .access_token)
```

Use it with `Authorization: Bearer $TOKEN` and `X-Tenant-ID: <uuid>` headers on all API calls.

---

## Modules

### 1. Auth (Keycloak OIDC)

Configures OAuth2 resource server security with Keycloak JWT authentication. Automatically syncs user records from JWT claims (email, name, picture) into the `cc.users` table on every authenticated request.

**Configuration:**

```yaml
cc:
  auth:
    realm: cc-platform        # Keycloak realm name
    resource: cc-backend       # Keycloak client ID
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| GET | `/cc/auth/me` | Get current authenticated user's identity and claims |
| GET | `/cc/auth/user/{id}` | Look up a synced user record by ID |

**Code Example:**

```java
@RestController
public class ProfileController {

    @GetMapping("/profile")
    public Map<String, Object> profile() {
        CcPrincipal principal = CcPrincipal.current();
        return Map.of(
            "userId", principal.getUserId(),
            "email", principal.getEmail(),
            "tenantId", principal.getTenantId(),
            "roles", principal.getRoles()
        );
    }
}
```

**curl:**

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/cc/auth/me
```

**Notes:**
- Public paths (no auth required): `/cc/health/**`, `/actuator/**`, `/swagger-ui/**`, `/v3/api-docs/**`
- Keycloak roles are prefixed with `ROLE_` in Spring Security authorities
- `JwtAuthConverter` extracts roles from the `realm_access.roles` JWT claim
- `UserSyncFilter` runs on every authenticated request to keep user records current

---

### 2. Multi-Tenancy

Resolves the current tenant from the `X-Tenant-ID` header or JWT `tenant_id` claim and stores it in a thread-local `TenantContext`. Supports multi-tenant and single-tenant modes.

**Configuration:**

```yaml
cc:
  tenancy:
    mode: multi                        # "multi" or "single"
    default-tenant-id:                 # UUID, required when mode=single
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| GET | `/cc/tenants/{id}` | Get tenant by ID |
| GET | `/cc/tenants/slug/{slug}` | Get tenant by URL-friendly slug |
| GET | `/cc/tenants/me` | List all tenants the current user belongs to |
| POST | `/cc/tenants` | Create a new tenant |
| PUT | `/cc/tenants/{id}` | Update tenant name and settings |
| POST | `/cc/tenants/{id}/onboard` | Full onboarding flow (tenant + admin user + roles) |

**Code Example:**

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping
    public List<Order> list() {
        UUID tenantId = TenantContext.require(); // throws if no tenant set
        return orderService.findByTenant(tenantId);
    }
}
```

**curl:**

```bash
# Create a tenant
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"name":"Acme Corp","slug":"acme-corp"}' \
     http://localhost:8080/cc/tenants

# List my tenants
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/cc/tenants/me
```

**Notes:**
- Tenant resolution order in multi-tenant mode: `X-Tenant-ID` header > `tenant_id` JWT claim > null
- In single-tenant mode, the filter always uses `cc.tenancy.default-tenant-id`
- Paths `/cc/health` and `/actuator/**` are excluded from tenant resolution
- Tenant ID is placed into MDC as `tenantId` for structured logging

---

### 3. RBAC

Role-based access control with Redis-cached permission checks (5-minute TTL). Use `@RequirePermission` for declarative enforcement or inject `RbacService` for programmatic checks. Permission format: `resource:action`.

**Configuration:**

```yaml
cc:
  rbac:
    enabled: true
    app-permissions:           # register your app's permissions
      - resource: orders
        actions: [read, create, update, delete]
      - resource: reports
        actions: [read, export]
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| GET | `/cc/rbac/permissions` | Get current user's permission keys |
| GET | `/cc/rbac/roles` | List system and tenant-specific roles |
| POST | `/cc/rbac/roles` | Create a new tenant-scoped role |
| POST | `/cc/rbac/roles/{roleId}/assign/{userId}` | Assign a role to a user |
| DELETE | `/cc/rbac/roles/{roleId}/revoke/{userId}` | Revoke a role from a user |
| GET | `/cc/rbac/check?resource=X&action=Y` | Check if user has a specific permission |

**Code Example:**

```java
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    // Declarative: annotation-based
    @RequirePermission(resource = "projects", action = "read")
    @GetMapping
    public List<Project> list() {
        return projectService.listAll();
    }

    @RequirePermission(resource = "projects", action = "create")
    @PostMapping
    public Project create(@RequestBody CreateProjectRequest request) {
        return projectService.create(request);
    }
}

// Programmatic: inject RbacService
@Service
public class ProjectService {

    private final RbacService rbacService;

    public void conditionalAction(UUID userId, UUID tenantId) {
        if (rbacService.hasPermission(userId, tenantId, "projects", "delete")) {
            // perform delete
        }
    }
}
```

**curl:**

```bash
# Check permission
curl -H "Authorization: Bearer $TOKEN" \
     -H "X-Tenant-ID: $TENANT_ID" \
     "http://localhost:8080/cc/rbac/check?resource=users&action=create"

# Create a role
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "X-Tenant-ID: $TENANT_ID" \
     -H "Content-Type: application/json" \
     -d '{"name":"project-manager","description":"Can manage projects","permissionIds":[1,5,12]}' \
     http://localhost:8080/cc/rbac/roles
```

**Notes:**
- Requires Redis for permission caching
- `@RequirePermission` requires a tenant in `TenantContext`
- System roles (`super_admin`, `org_admin`, `member`) cannot be deleted
- Cache is auto-invalidated when roles are assigned or removed
- Wildcard `*:*` grants all permissions

---

### 4. Feature Flags

Tenant-aware feature flags with a 4-level override hierarchy: user > tenant > global > default. Evaluations are cached in Redis with a 5-minute TTL.

**Configuration:**

```yaml
cc:
  flags:
    enabled: true
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| GET | `/cc/flags?tenantId=X&userId=Y` | Evaluate all flags for a tenant/user |
| GET | `/cc/flags/{key}?tenantId=X&userId=Y` | Check if a specific flag is enabled |
| POST | `/cc/flags` | Create a new feature flag |
| PUT | `/cc/flags/{key}/override` | Set a tenant/user-specific override |

**Code Example:**

```java
@RestController
public class FeatureController {

    private final FeatureFlagService flagService;

    @GetMapping("/api/dashboard")
    public Map<String, Object> dashboard() {
        UUID tenantId = TenantContext.require();
        UUID userId = CcPrincipal.current().getUserId();

        boolean newUi = flagService.isEnabled("new-dashboard-ui", tenantId, userId);
        Map<String, Boolean> allFlags = flagService.evaluateAll(tenantId, userId);

        return Map.of("useNewUi", newUi, "flags", allFlags);
    }
}
```

**curl:**

```bash
# Evaluate all flags
curl -H "Authorization: Bearer $TOKEN" \
     "http://localhost:8080/cc/flags?tenantId=$TENANT_ID&userId=$USER_ID"

# Create a flag
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"key":"dark-mode","description":"Enable dark mode","defaultEnabled":false}' \
     http://localhost:8080/cc/flags

# Set override for a tenant
curl -X PUT -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"tenantId":"660e8400-...","enabled":true}' \
     http://localhost:8080/cc/flags/dark-mode/override
```

**Notes:**
- Requires Redis for evaluation caching
- `isEnabled()` throws `CcException(RESOURCE_NOT_FOUND)` if the flag key does not exist
- Cache is auto-invalidated when overrides are set

---

### 5. Audit Logging

3-layer audit system: (1) automatic system audit of all HTTP requests via `SystemAuditFilter`, (2) opt-in business audit via `@Audited` annotation or `BusinessAuditService`, and (3) auth event tracking.

**Configuration:**

```yaml
cc:
  audit:
    system-audit-enabled: true
    auth-event-sync-enabled: true
    log-request-body: true
    log-response-body: false
    sensitive-fields: password,secret,token,apiKey,authorization
    exclude-paths: /actuator/**,/cc/health
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| GET | `/cc/audit/system` | Query paginated system audit logs |
| GET | `/cc/audit/business` | Query paginated business audit logs |
| GET | `/cc/audit/auth` | Query paginated auth events |
| GET | `/cc/audit/resource/{type}/{id}` | Combined audit logs for a resource |
| GET | `/cc/audit/user/{userId}` | Combined audit logs for a user |

All audit endpoints support pagination: `?page=0&size=20&sort=createdAt,desc`

**Code Example:**

```java
@Service
public class OrderService {

    private final BusinessAuditService auditService;

    // Option 1: Annotation-based
    @Audited(action = "UPDATE", resource = "order")
    public Order updateOrder(UUID orderId, UpdateOrderRequest request) {
        return orderRepository.save(/* ... */);
    }

    // Option 2: Programmatic
    public void cancelOrder(UUID orderId) {
        Order before = orderRepository.findById(orderId).orElseThrow();
        before.setStatus("cancelled");
        Order after = orderRepository.save(before);

        auditService.record(
            TenantContext.require(),
            CcPrincipal.current().getUserId(),
            "CANCEL", "order", orderId,
            before, after
        );
    }
}
```

**curl:**

```bash
# Query system audit logs
curl -H "Authorization: Bearer $TOKEN" \
     "http://localhost:8080/cc/audit/system?tenantId=$TENANT_ID&page=0&size=10"

# Query audit for a specific user
curl -H "Authorization: Bearer $TOKEN" \
     "http://localhost:8080/cc/audit/user/$USER_ID"
```

**Notes:**
- System audit automatically captures: HTTP method, path, status, duration, user ID, tenant ID, IP, user agent, correlation ID
- Sensitive fields in request bodies are replaced with `***` before storage
- Both audit layers use the correlation ID from MDC

---

### 6. Notifications

Multi-channel notifications (in-app, email) with per-user preference management. Pluggable channel architecture -- implement `NotificationChannel` to add custom channels (Slack, SMS, push).

**Configuration:**

```yaml
cc:
  notifications:
    enabled: true
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| GET | `/cc/notifications?userId=X` | List user notifications (paginated) |
| PUT | `/cc/notifications/{id}/read` | Mark a notification as read |
| POST | `/cc/notifications/send` | Send a notification via a channel |
| GET | `/cc/notifications/preferences?userId=X` | Get user channel preferences |
| PUT | `/cc/notifications/preferences` | Update a channel preference |

**Code Example:**

```java
@Service
public class OrderNotifier {

    private final NotificationService notificationService;

    public void notifyOrderShipped(UUID userId, UUID tenantId, String orderId) {
        notificationService.send(
            userId, tenantId,
            "in_app",
            "Order Shipped",
            "Your order #" + orderId + " has been shipped.",
            "{\"orderId\":\"" + orderId + "\"}"
        );
    }
}

// Custom channel implementation
@Component
public class SlackChannel implements NotificationChannel {
    @Override
    public String channelName() { return "slack"; }

    @Override
    public void send(Notification notification) {
        // Send via Slack API
    }
}
```

**curl:**

```bash
# Send a notification
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "X-Tenant-ID: $TENANT_ID" \
     -H "Content-Type: application/json" \
     -d '{"userId":"550e8400-...","channel":"in_app","title":"Hello","body":"World"}' \
     http://localhost:8080/cc/notifications/send

# Update preference
curl -X PUT -H "Authorization: Bearer $TOKEN" \
     -H "X-Tenant-ID: $TENANT_ID" \
     -H "Content-Type: application/json" \
     -d '{"userId":"550e8400-...","channel":"email","enabled":false}' \
     http://localhost:8080/cc/notifications/preferences
```

**Notes:**
- `send()` checks user preferences before dispatching; throws `VALIDATION_ERROR` if the user has disabled the channel
- Unknown channel names throw a validation error

---

### 7. Webhooks

Dispatches webhook events to tenant-registered HTTP endpoints with HMAC-SHA256 signature support, delivery tracking, and retry capability.

**Configuration:**

```yaml
cc:
  webhooks:
    enabled: true
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| POST | `/cc/webhooks/endpoints` | Register a webhook endpoint |
| GET | `/cc/webhooks/endpoints?tenantId=X` | List tenant's webhook endpoints |
| DELETE | `/cc/webhooks/endpoints/{id}` | Remove a webhook endpoint |
| GET | `/cc/webhooks/deliveries?endpointId=X` | View delivery history (paginated) |
| POST | `/cc/webhooks/deliveries/{id}/retry` | Retry a failed delivery |

**Code Example:**

```java
@Service
public class OrderWebhooks {

    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public void onOrderCompleted(UUID tenantId, Order order) throws Exception {
        String payload = objectMapper.writeValueAsString(order);
        webhookService.dispatch(tenantId, "order.completed", payload);
    }
}
```

**curl:**

```bash
# Register a webhook endpoint
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"tenantId":"660e8400-...","url":"https://hooks.example.com/cc","secret":"whsec_abc","eventTypes":"order.completed,order.cancelled"}' \
     http://localhost:8080/cc/webhooks/endpoints
```

**Notes:**
- Only active endpoints receive dispatches
- Delivery records are created with status `"pending"`
- `sign()` computes HMAC-SHA256 hex signatures using the endpoint secret

---

### 8. Queue / Background Jobs

PostgreSQL-backed job queue with `SKIP LOCKED` dequeuing, automatic retry (up to 5 attempts), dead letter support, and a scheduled worker that polls registered `JobHandler` implementations.

**Configuration:**

```yaml
cc:
  queue:
    worker-enabled: true       # false on API-only nodes
    poll-interval-ms: 1000     # polling interval
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| GET | `/cc/queues/stats` | Queue depth and processing stats |
| GET | `/cc/queues/{name}/dead-letter` | List dead-letter jobs for a queue |
| POST | `/cc/queues/{name}/dead-letter/{id}/retry` | Re-enqueue a dead-letter job |

**Code Example:**

```java
// 1. Define a handler
@Component
public class EmailJobHandler implements JobHandler {

    @Override
    public String getQueueName() { return "email"; }

    @Override
    public void handle(Job job) {
        String payload = job.getPayload();
        // ... send email logic
    }
}

// 2. Enqueue from anywhere
@Service
public class EmailService {

    private final QueueService queueService;

    public void sendLater(String emailJson) {
        queueService.enqueue("email", emailJson, 0); // 0 = no delay
    }

    public void sendDelayed(String emailJson, int delaySeconds) {
        queueService.enqueue("email", emailJson, delaySeconds);
    }
}
```

**curl:**

```bash
# View queue stats
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/cc/queues/stats

# Retry a dead-letter job
curl -X POST -H "Authorization: Bearer $TOKEN" \
     http://localhost:8080/cc/queues/notifications/dead-letter/42/retry
```

**Notes:**
- Job lifecycle: `pending` -> `processing` -> `completed` | `failed` | `retry` -> (after 5 attempts) `dead_letter`
- Uses PostgreSQL `FOR UPDATE SKIP LOCKED` for safe concurrent dequeuing
- Set `cc.queue.worker-enabled=false` on nodes that should only enqueue, not process

---

### 9. Search (Full-Text + Vector)

Full-text search (PostgreSQL `tsvector`), vector similarity search (`pgvector` cosine distance), and hybrid search combining both with configurable weights.

**Configuration:**

No `cc.*` properties needed. Always active when JDBC is available.

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| POST | `/cc/search/index` | Add/update a resource in the search index |
| GET | `/cc/search?tenantId=X&q=Y` | Full-text search |
| POST | `/cc/search/semantic` | Vector similarity search |
| POST | `/cc/search/hybrid` | Combined text + vector search |
| DELETE | `/cc/search/index` | Remove a resource from the index |

**Code Example:**

```java
@Service
public class ArticleIndexer {

    private final SearchService searchService;

    public void indexArticle(UUID tenantId, Article article) {
        searchService.index(
            tenantId, "article", article.getId(),
            article.getTitle() + " " + article.getBody(),
            "{\"author\": \"" + article.getAuthor() + "\"}"
        );
    }

    public List<SearchResult> search(UUID tenantId, String query) {
        return searchService.search(tenantId, query, "article", 20);
    }

    public List<SearchResult> hybridSearch(UUID tenantId, String query, double[] embedding) {
        return searchService.hybridSearch(tenantId, query, embedding, 0.7, 0.3, "article", 10);
    }
}
```

**curl:**

```bash
# Full-text search
curl -H "Authorization: Bearer $TOKEN" \
     "http://localhost:8080/cc/search?tenantId=$TENANT_ID&q=financial+report&type=document&limit=10"

# Index a resource
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"tenantId":"660e8400-...","resourceType":"document","resourceId":"cc0e8400-...","content":"Q1 report..."}' \
     http://localhost:8080/cc/search/index
```

**Notes:**
- Full-text uses `plainto_tsquery('english', ...)` with `ts_rank` scoring
- Vector search uses the `<=>` cosine distance operator from pgvector
- Pass `null` for `resourceType` to search across all types
- Use with the AI module to generate embeddings for semantic search

---

### 10. Storage (MinIO / S3)

Tenant-scoped file storage via MinIO or any S3-compatible endpoint. All object keys are prefixed with the tenant ID for isolation. Provides presigned URL generation for upload/download.

**Configuration:**

```yaml
cc:
  storage:
    endpoint: http://localhost:9000
    access-key: minioadmin
    secret-key: minioadmin
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| POST | `/cc/storage/upload-url` | Generate a presigned upload URL |
| POST | `/cc/storage/download-url` | Generate a presigned download URL |
| GET | `/cc/storage/files?tenantId=X&bucket=Y` | List files in a bucket |
| DELETE | `/cc/storage/files` | Delete a file |

**Code Example:**

```java
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final StorageService storageService;

    @PostMapping("/upload-url")
    public Map<String, String> getUploadUrl(@RequestParam String fileName,
                                             @RequestParam String contentType) {
        UUID tenantId = TenantContext.require();
        String url = storageService.getUploadUrl(tenantId, "documents", fileName, contentType, 3600);
        return Map.of("uploadUrl", url);
    }

    @GetMapping
    public List<StorageService.FileInfo> listFiles(@RequestParam(required = false) String prefix) {
        UUID tenantId = TenantContext.require();
        return storageService.listFiles(tenantId, "documents", prefix);
    }
}
```

**curl:**

```bash
# Get a presigned upload URL
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"tenantId":"660e8400-...","bucket":"documents","fileName":"report.pdf","contentType":"application/pdf"}' \
     http://localhost:8080/cc/storage/upload-url
```

**Notes:**
- Object key pattern: `{tenantId}/{bucket}/{fileName}`
- Works with MinIO, AWS S3, DigitalOcean Spaces, or any S3-compatible service
- Presigned URLs have configurable expiry in seconds
- Module only activates when `cc.storage.endpoint` is set

---

### 11. Export (Async CSV/JSON)

Manages asynchronous export and import jobs with status tracking. Creates job records that are processed by a queue worker.

**Configuration:**

```yaml
cc:
  export:
    enabled: true
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| POST | `/cc/export` | Start an async export job |
| GET | `/cc/export/{jobId}` | Get export job status and download URL |
| POST | `/cc/export/import` | Start an async import job |
| GET | `/cc/export/jobs?tenantId=X` | List all export/import jobs (paginated) |

**Code Example:**

```java
@Service
public class ReportExporter {

    private final ExportService exportService;

    public UUID exportUsers(UUID tenantId) {
        return exportService.startExport(tenantId, "users", "csv", "status=active", "id,email,displayName");
    }

    public ExportJob checkStatus(UUID jobId) {
        return exportService.getJobStatus(jobId);
    }
}
```

**curl:**

```bash
# Start an export
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"tenantId":"660e8400-...","resourceType":"users","format":"csv","columns":"id,email,displayName"}' \
     http://localhost:8080/cc/export

# Check status
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/cc/export/$JOB_ID
```

**Notes:**
- Job status values: `pending`, `processing`, `completed`, `failed`
- This module manages job metadata only; implement actual export logic in a `JobHandler`
- Use with the Storage module to store exported files
- The `resultUrl` field stores the download URL after completion

---

### 12. Security (Rate Limiting + Input Sanitization + Encryption)

Rate limiting (Redis-backed, per client IP), CORS configuration, AES-256-GCM field-level encryption, and input sanitization utilities.

**Configuration:**

```yaml
cc:
  security:
    rate-limit-enabled: true
    default-rate-limit: 100               # requests per minute per IP
    cors-origins: http://localhost:3000,http://localhost:5173
    encryption-key: "0123456789abcdef..."  # 64-char hex string (32 bytes AES-256)
```

**REST API Endpoints:**

No dedicated endpoints. Rate limiting works automatically via servlet filter. Clients receive response headers:

| Header | Description |
|--------|-------------|
| `X-RateLimit-Limit` | Max requests per window |
| `X-RateLimit-Remaining` | Requests remaining |
| `X-RateLimit-Reset` | Seconds until window resets |

Returns HTTP 429 when the limit is exceeded.

**Code Example:**

```java
// Encryption (inject when cc.security.encryption-key is set)
@Service
public class SecretStore {

    private final EncryptionService encryptionService;

    public String encryptSecret(String plaintext) {
        return encryptionService.encrypt(plaintext);  // returns Base64 string
    }

    public String decryptSecret(String ciphertext) {
        return encryptionService.decrypt(ciphertext);
    }
}

// Input sanitization (static methods, no injection needed)
String safe = InputSanitizer.sanitizeHtml(userInput);    // strips HTML tags
String safeSql = InputSanitizer.sanitizeSql(userInput);  // escapes single quotes
```

**Notes:**
- Rate limiting requires Redis (`StringRedisTemplate` bean)
- Encryption key must be exactly 64 hex characters (32 bytes for AES-256)
- `InputSanitizer` is defense-in-depth; always use parameterized queries
- CORS allows `GET, POST, PUT, DELETE, PATCH, OPTIONS` with credentials and a 1-hour max age

---

### 13. AI Gateway (LiteLLM)

Client for the LiteLLM AI gateway, supporting chat completions, embeddings, and model listing. Uses the OpenAI-compatible API format. Routes to OpenAI, Anthropic, Google Gemini, and more via LiteLLM.

**Configuration:**

```yaml
cc:
  ai:
    gateway-url: http://localhost:4000
    master-key: sk-dev
```

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| POST | `/cc/ai/chat` | Chat completion request |
| POST | `/cc/ai/embed` | Generate a vector embedding |
| GET | `/cc/ai/models` | List available AI models |

**Code Example:**

```java
@Service
public class SummaryService {

    private final AiService aiService;

    public String summarize(String document) {
        ChatRequest request = new ChatRequest(
            "gpt-4",
            List.of(
                new ChatMessage("system", "You are a helpful assistant."),
                new ChatMessage("user", "Summarize this: " + document)
            ),
            0.7,   // temperature
            1000   // maxTokens
        );
        ChatResponse response = aiService.chat(request);
        return response.content();
    }

    public List<Double> generateEmbedding(String text) {
        return aiService.embed("text-embedding-3-small", text);
    }
}
```

**curl:**

```bash
# Chat completion
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"model":"gpt-4","messages":[{"role":"user","content":"Hello"}]}' \
     http://localhost:8080/cc/ai/chat

# List models
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/cc/ai/models
```

**Notes:**
- Module only activates when `cc.ai.gateway-url` is set
- LiteLLM requires at least one provider API key (`OPENAI_API_KEY`, `ANTHROPIC_API_KEY`, or `GEMINI_API_KEY`)
- Use `embed()` with the Search module for semantic/hybrid search

---

### 14. Payments

Payment abstraction via the `PaymentProvider` interface. Ships with a `NoOpPaymentProvider` for development; replace with your Stripe/payment implementation by defining a `PaymentProvider` bean.

**Configuration:**

No `cc.*` properties. Always active. Define a `PaymentProvider` `@Component` to replace the no-op default.

**REST API Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| POST | `/cc/payments/intents` | Create a payment intent |
| POST | `/cc/payments/subscriptions` | Create a subscription |
| DELETE | `/cc/payments/subscriptions/{id}` | Cancel a subscription |
| POST | `/cc/payments/webhooks` | Handle payment provider webhooks |

**Code Example:**

```java
// Implement PaymentProvider for your processor
@Component
public class StripePaymentProvider implements PaymentProvider {

    @Override
    public PaymentIntent createPaymentIntent(long amount, String currency, Map<String, String> metadata) {
        // Stripe API call
    }

    @Override
    public Subscription createSubscription(String customerId, String priceId) {
        // Stripe API call
    }

    @Override
    public void cancelSubscription(String subscriptionId) {
        // Stripe API call
    }

    @Override
    public boolean verifyWebhookSignature(String payload, String signature) {
        // Stripe signature verification
    }

    @Override
    public Map<String, Object> handleWebhook(String payload) {
        // Parse and process Stripe event
    }
}
```

**curl:**

```bash
# Create a payment intent
curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"amount":2999,"currency":"usd","metadata":{"orderId":"ORD-123"}}' \
     http://localhost:8080/cc/payments/intents
```

**Notes:**
- `NoOpPaymentProvider` returns stub data -- replace it by defining your own `@Component`
- Your bean takes precedence automatically via `@ConditionalOnMissingBean`
- Payment webhook endpoint uses provider signature verification, not JWT auth

---

### 15. Logging (Correlation IDs)

Adds a correlation ID to every request and enriches all log output with `correlationId`, `tenantId`, and `userId` via MDC. Supports structured JSON logging.

**Configuration:**

The log pattern is set automatically by `cc-defaults.yml`:

```
%5p [%X{correlationId}] [%X{tenantId}] [%X{userId}]
```

For structured JSON logging:

```yaml
logging:
  structured:
    format:
      console: ecs  # or logstash, gelf
```

**REST API Endpoints:**

No dedicated endpoints. The `CorrelationIdFilter` (order `HIGHEST_PRECEDENCE + 10`) runs on every request.

**Code Example:**

```java
// Propagate a correlation ID from upstream
// Send header: X-Correlation-ID: <uuid>
// If missing, a new UUID is auto-generated.

// Access programmatically
import org.slf4j.MDC;

String correlationId = MDC.get("correlationId");
String tenantId = MDC.get("tenantId");
String userId = MDC.get("userId");
```

**Notes:**
- This filter runs first (before tenant and auth filters)
- The correlation ID is returned in the `X-Correlation-ID` response header
- MDC is cleaned up in the `finally` block to prevent thread-local leaks
- Links system audit logs, business audit logs, and application logs together

---

### 16. Error Handling

Global exception handler (`@RestControllerAdvice`) that converts all exceptions into consistent JSON error responses with correlation IDs.

**Configuration:**

No properties. Always active.

**REST API Endpoints:**

No dedicated endpoints. All errors from any endpoint are caught and formatted.

**Error Response Format:**

```json
{
  "error": "RESOURCE_NOT_FOUND",
  "message": "Order not found: 123",
  "correlationId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2026-03-17T10:30:00Z",
  "details": {}
}
```

**Code Example:**

```java
// Throw CcException using factory methods
throw CcErrorCodes.resourceNotFound("Order not found: " + orderId);
throw CcErrorCodes.forbidden("Only admins can perform this action");
throw CcErrorCodes.validationError("Email format is invalid");

// Custom error with explicit status
throw new CcException("CUSTOM_ERROR", "Something specific went wrong", 422);
```

**Error Codes:**

| Code | HTTP Status | Factory Method |
|------|-------------|----------------|
| `UNAUTHORIZED` | 401 | `CcErrorCodes.unauthorized(msg)` |
| `FORBIDDEN` | 403 | `CcErrorCodes.forbidden(msg)` |
| `PERMISSION_DENIED` | 403 | `CcErrorCodes.permissionDenied(msg)` |
| `TENANT_NOT_FOUND` | 404 | `CcErrorCodes.tenantNotFound(msg)` |
| `TENANT_DISABLED` | 403 | `CcErrorCodes.tenantDisabled(msg)` |
| `RESOURCE_NOT_FOUND` | 404 | `CcErrorCodes.resourceNotFound(msg)` |
| `RESOURCE_CONFLICT` | 409 | `CcErrorCodes.resourceConflict(msg)` |
| `VALIDATION_ERROR` | 400 | `CcErrorCodes.validationError(msg)` |
| `RATE_LIMITED` | 429 | `CcErrorCodes.rateLimited(msg)` |
| `INTERNAL_ERROR` | 500 | `CcErrorCodes.internalError(msg)` |

**Notes:**
- Every error response includes the correlation ID from the Logging module
- 5xx errors are logged at ERROR level; 4xx at WARN level
- Unhandled exceptions return `INTERNAL_ERROR` with no stack trace leaked to clients
- Validation errors include per-field messages in the `details` map

---

## Configuration Reference

All properties under the `cc.*` namespace, bound via `CcProperties`:

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `cc.tenancy.mode` | String | `"multi"` | `"multi"` or `"single"` |
| `cc.tenancy.default-tenant-id` | UUID | `null` | Tenant ID for single-tenant mode |
| `cc.auth.realm` | String | `"cc-platform"` | Keycloak realm name |
| `cc.auth.resource` | String | `"cc-backend"` | Keycloak client ID |
| `cc.rbac.enabled` | boolean | `true` | Enable/disable RBAC module |
| `cc.rbac.app-permissions` | List | `[]` | Register app-specific permissions |
| `cc.flags.enabled` | boolean | `true` | Enable/disable feature flags |
| `cc.notifications.enabled` | boolean | `true` | Enable/disable notifications |
| `cc.webhooks.enabled` | boolean | `true` | Enable/disable webhooks |
| `cc.export.enabled` | boolean | `true` | Enable/disable export module |
| `cc.queue.worker-enabled` | boolean | `true` | Enable/disable job queue worker |
| `cc.queue.poll-interval-ms` | int | `1000` | Worker polling interval (ms) |
| `cc.audit.system-audit-enabled` | boolean | `true` | Enable system audit filter |
| `cc.audit.auth-event-sync-enabled` | boolean | `true` | Enable auth event sync |
| `cc.audit.log-request-body` | boolean | `true` | Capture request bodies in audit |
| `cc.audit.log-response-body` | boolean | `false` | Capture response bodies in audit |
| `cc.audit.sensitive-fields` | List\<String\> | `password,secret,token,apiKey,authorization` | Fields to redact in audit |
| `cc.audit.exclude-paths` | List\<String\> | `/actuator/**,/cc/health` | Paths to exclude from audit |
| `cc.security.rate-limit-enabled` | boolean | `true` | Enable Redis rate limiting |
| `cc.security.default-rate-limit` | int | `100` | Requests per minute per IP |
| `cc.security.cors-origins` | List\<String\> | `http://localhost:3000,http://localhost:5173` | Allowed CORS origins |
| `cc.security.encryption-key` | String | `null` | 64-char hex string for AES-256-GCM |
| `cc.storage.endpoint` | String | `"http://localhost:9000"` | MinIO/S3 endpoint URL |
| `cc.storage.access-key` | String | `"minioadmin"` | Storage access key |
| `cc.storage.secret-key` | String | `"minioadmin"` | Storage secret key |
| `cc.ai.gateway-url` | String | `"http://localhost:4000"` | LiteLLM gateway URL |
| `cc.ai.master-key` | String | `"sk-dev"` | LiteLLM API key |

**Spring Boot defaults set by cc-defaults.yml:**

| Property | Default | Description |
|----------|---------|-------------|
| `spring.flyway.locations` | `classpath:db/migration/cc,classpath:db/migration/app` | Migration script locations |
| `spring.flyway.schemas` | `cc,audit,tenant` | Managed PostgreSQL schemas |
| `logging.pattern.level` | `%5p [%X{correlationId}] [%X{tenantId}] [%X{userId}]` | Structured log pattern |

---

## Infrastructure

### Required Docker Services

Start all with `cd cc-platform && docker compose up -d`:

| Service | Image | Host Port | Purpose |
|---------|-------|-----------|---------|
| PostgreSQL | `pgvector/pgvector:pg16` | 5432 | Primary database with pgvector extension |
| Redis | `redis:7-alpine` | 6379 | Caching, rate limiting, RBAC/flag cache |
| Keycloak | `quay.io/keycloak/keycloak:24.0` | 8180 | Identity provider (SSO, OIDC, JWT) |
| MinIO | `minio/minio:latest` | 9000 (API), 9001 (Console) | S3-compatible object storage |
| LiteLLM | `ghcr.io/berriai/litellm:main-latest` | 4000 | LLM gateway proxy (optional) |

### Default Credentials

| Service | Username | Password | Notes |
|---------|----------|----------|-------|
| PostgreSQL | `cc` | `cc_dev` | Database: `crosscutting` |
| Keycloak Admin | `admin` | `admin` | Console: `http://localhost:8180` |
| MinIO | `minioadmin` | `minioadmin` | Console: `http://localhost:9001` |
| LiteLLM | -- | `sk-dev` (master key) | API: `http://localhost:4000` |

### Keycloak Realm

The `cc-platform` realm is auto-imported with:

- **Clients:** `cc-api` (confidential, secret: `cc-api-secret`) and `cc-web` (public SPA)
- **Realm Roles:** `super_admin`, `org_admin`, `member` (default)
- **Token lifespan:** 5 minutes
- **Protocol mapper:** includes `realm_roles` claim in JWT access tokens

### PostgreSQL Schemas

| Schema | Purpose |
|--------|---------|
| `cc` | Core platform tables |
| `audit` | Audit log tables |
| `tenant` | Reserved for tenant-specific application data |

### MinIO Buckets (auto-created)

| Bucket | Purpose |
|--------|---------|
| `uploads` | User-uploaded files |
| `exports` | Async export output |
| `avatars` | Profile images |

### Docker in Docker

If your app runs inside Docker on the same network as cc-platform, use container hostnames:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/crosscutting
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://keycloak:8180/realms/cc-platform
  data:
    redis:
      host: redis
cc:
  storage:
    endpoint: http://minio:9000
```

---

## Database Migrations

Flyway runs automatically on startup. Migrations are bundled in the JAR at `classpath:db/migration/cc`:

| Version | Name | What It Creates |
|---------|------|-----------------|
| V0 | `create_schemas` | `cc`, `audit`, `tenant` schemas |
| V1 | `core_tenancy` | `cc.tenants`, `cc.users`, `cc.tenant_memberships`, `cc.tenant_config` |
| V2 | `rbac` | `cc.roles`, `cc.permissions`, `cc.role_permissions`, `cc.user_roles` |
| V3 | `audit` | Audit log tables in the `audit` schema |
| V4 | `feature_flags` | `cc.feature_flags`, `cc.feature_flag_overrides` |
| V5 | `notifications` | Notification and preference tables |
| V6 | `webhooks` | Webhook endpoint and delivery tables |
| V7 | `queue` | Background job queue (SKIP LOCKED) |
| V8 | `search_vectors` | Full-text search and pgvector embedding tables |
| V9 | `seed_role_permissions` | Seeds system roles (`super_admin`, `org_admin`, `member`) with default permissions |
| V10 | `add_foreign_keys` | Additional foreign key constraints and indexes |
| V11 | `fix_schema_mismatches` | Schema alignment fixes |
| V12 | `create_export_jobs` | `cc.export_jobs` table |
| V13 | `platform_bootstrap` | Platform bootstrap data |
| V14 | `fix_flag_override_check` | Fix flag override constraint |
| V15 | `fix_webhook_delivery_defaults` | Fix webhook delivery column defaults |

### Adding Your Own Migrations

Place application-specific migrations in `src/main/resources/db/migration/app/`:

```
src/main/resources/db/migration/app/
  V1__create_orders_table.sql
  V2__add_order_items.sql
```

Flyway scans both `classpath:db/migration/cc` (from cc-starter) and `classpath:db/migration/app` (from your app).
