# APIEvolver Detailed Spec

## Entities
1. **ApiSpec**
   - `id` (UUID): Primary key
   - `tenantId` (UUID): Tenant scope
   - `name` (varchar(180)): Display name
   - `status` (varchar(40)): Enum (DRAFT, ACTIVE, DEPRECATED)
   - `metadataJson` (jsonb): Additional info
   - `createdAt` (timestamptz)
   - `updatedAt` (timestamptz)

2. **ApiVersion**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `name` (varchar(180))
   - `status` (varchar(40)): Enum (PUBLISHED, RETIRED)
   - `metadataJson` (jsonb)
   - `createdAt` (timestamptz)
   - `updatedAt` (timestamptz)

3. **BreakingChange**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `name` (varchar(180))
   - `status` (varchar(40)): Enum (DETECTED, RESOLVED, IGNORED)
   - `metadataJson` (jsonb)
   - `createdAt` (timestamptz)
   - `updatedAt` (timestamptz)

4. **CompatibilityReport**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `name` (varchar(180))
   - `status` (varchar(40)): Enum (GENERATED, REVIEWED)
   - `metadataJson` (jsonb)
   - `createdAt` (timestamptz)
   - `updatedAt` (timestamptz)

5. **DeprecationNotice**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `name` (varchar(180))
   - `status` (varchar(40)): Enum (ACTIVE, CANCELLED, COMPLETED)
   - `metadataJson` (jsonb)
   - `createdAt` (timestamptz)
   - `updatedAt` (timestamptz)

6. **SdkArtifact**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `name` (varchar(180))
   - `status` (varchar(40)): Enum (BUILDING, READY, FAILED)
   - `metadataJson` (jsonb)
   - `createdAt` (timestamptz)
   - `updatedAt` (timestamptz)

## Endpoints

Base path: `/api/v1/api-evolution`

- **ApiSpec:** `GET /api-specs`, `POST /api-specs`, `GET /api-specs/{id}`, `PATCH /api-specs/{id}`, `POST /api-specs/{id}/validate`
- **ApiVersion:** `GET /api-versions`, `POST /api-versions`, `GET /api-versions/{id}`, `PATCH /api-versions/{id}`, `POST /api-versions/{id}/validate`
- **BreakingChange:** `GET /breaking-changes`, `POST /breaking-changes`, `GET /breaking-changes/{id}`, `PATCH /breaking-changes/{id}`, `POST /breaking-changes/{id}/validate`
- **CompatibilityReport:** `GET /compatibility-reports`, `POST /compatibility-reports`, `GET /compatibility-reports/{id}`, `PATCH /compatibility-reports/{id}`, `POST /compatibility-reports/{id}/validate`
- **DeprecationNotice:** `GET /deprecation-notices`, `POST /deprecation-notices`, `GET /deprecation-notices/{id}`, `PATCH /deprecation-notices/{id}`, `POST /deprecation-notices/{id}/validate`
- **AI/Workflows:** `POST /ai/analyze`, `POST /workflows/execute`, `GET /metrics/summary`

## AI Integration
- Call LiteLLM via gateway for `/ai/analyze` with retry/circuit-breaker.

## Frontend
- Replace home with Next.js specific pages for each module under `src/app/` with relevant `List` components in `src/components/`.
- Tests required for each component and primary page flow.

## Events
- Emits: `apievolver.breaking-change.detected`, `apievolver.version.published`, `apievolver.sdk.generated`
- Consumes: `apimanager.version.published`, `dependencyradar.scan.completed`, `securitypulse.finding.created`

## Authentication
- Multi-tenant via `X-Tenant-ID` header.
