# ProspectIQ Detailed Specification

## Core Domain Entities

1. **Prospect**
   - Represents a potential target account or person.
   - `id`: UUID (PK)
   - `tenant_id`: UUID
   - `name`: String
   - `domain`: String
   - `industry`: String
   - `region`: String
   - `crm_id`: String (Nullable)
   - `created_at`, `updated_at`: Timestamps

2. **ICPProfile (Ideal Customer Profile)**
   - `id`: UUID (PK)
   - `tenant_id`: UUID
   - `name`: String
   - `criteria`: JSONB (key-value pairs, e.g. {"min_funding": "10M", "industries": ["tech", "finance"]})
   - `created_at`, `updated_at`: Timestamps

3. **Signal**
   - Represents an intelligence signal about a prospect.
   - `id`: UUID (PK)
   - `tenant_id`: UUID
   - `prospect_id`: UUID (FK)
   - `type`: String (FUNDING, HIRING, TECH_STACK, LEADERSHIP, NEWS, REVIEWS)
   - `source`: String (e.g. "TechCrunch", "LinkedIn")
   - `content`: Text (e.g. "Raised Series B of $50M")
   - `detected_at`: Timestamp
   - `created_at`: Timestamp

4. **ProspectBrief**
   - AI generated briefing for a prospect.
   - `id`: UUID (PK)
   - `tenant_id`: UUID
   - `prospect_id`: UUID (FK)
   - `content`: Text (Markdown format)
   - `status`: String (PENDING, GENERATED, FAILED)
   - `created_at`, `updated_at`: Timestamps

## REST Endpoints (Base Path: `/api/v1`)

### Prospects
- `GET /prospects`: List prospects (supports filtering by industry, region). Response: `List<Prospect>`
- `GET /prospects/{id}`: Get prospect details. Response: `Prospect`
- `POST /prospects`: Create a prospect. Request: `{name, domain, industry, region}`. Response: `Prospect`
- `POST /prospects/{id}/sync`: Sync prospect with CRM. Request: `{}`

### ICP Profiles
- `GET /icp`: List profiles.
- `POST /icp`: Create ICP profile. Request: `{name, criteria: {}}`

### Signals
- `GET /prospects/{id}/signals`: Get signals for a prospect.
- `POST /signals/ingest`: Ingest new signal. Request: `{prospectId, type, source, content, detectedAt}`

### Briefs
- `POST /prospects/{id}/briefs`: Generate brief.
- `GET /prospects/{id}/briefs`: Get latest brief.
- `GET /briefs/{id}/export/pdf`: Export brief as PDF.

## Next.js Frontend Routes

- `/`: Dashboard showing overall prospect stats, recent signals.
- `/prospects`: List of prospects.
- `/prospects/[id]`: Detail view of a prospect with signals and AI brief.
- `/icp`: Page to define and manage ICP profiles.

## AI Integration

- Use `LiteLLMClient` to orchestrate calling AI (claude-sonnet-4-6).
- Prompt will include the `Prospect` details and their latest `Signal`s to generate a comprehensive sales brief.

## Background Jobs

- Integration webhook handler for CRM sync (handled via `ApplicationEventPublisher`).

