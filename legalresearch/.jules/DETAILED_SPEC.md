# LegalResearch — Detailed Spec

## 1) Overview
This document extends the base specification for the `legalresearch` app. It provides detailed schema information, service behavior, and REST API definitions necessary for a complete implementation.

## 2) Database Schema
All domain models include multi-tenant support via a `tenant_id` UUID column.

- **ResearchQuery**
  - id: UUID
  - tenant_id: UUID
  - name: varchar(180)
  - status: varchar(40)
  - metadata_json: jsonb
  - created_at: timestamptz
  - updated_at: timestamptz

- **SourceCitation**
  - id: UUID
  - tenant_id: UUID
  - name: varchar(180)
  - status: varchar(40)
  - metadata_json: jsonb
  - created_at: timestamptz
  - updated_at: timestamptz

- **PrecedentNote**
  - id: UUID
  - tenant_id: UUID
  - name: varchar(180)
  - status: varchar(40)
  - metadata_json: jsonb
  - created_at: timestamptz
  - updated_at: timestamptz

- **MemoDraft**
  - id: UUID
  - tenant_id: UUID
  - name: varchar(180)
  - status: varchar(40)
  - metadata_json: jsonb
  - created_at: timestamptz
  - updated_at: timestamptz

- **ArgumentGraph**
  - id: UUID
  - tenant_id: UUID
  - name: varchar(180)
  - status: varchar(40)
  - metadata_json: jsonb
  - created_at: timestamptz
  - updated_at: timestamptz

- **ReviewComment**
  - id: UUID
  - tenant_id: UUID
  - name: varchar(180)
  - status: varchar(40)
  - metadata_json: jsonb
  - created_at: timestamptz
  - updated_at: timestamptz

## 3) REST API Contract

For each entity (`research-queries`, `source-citations`, `precedent-notes`, `memo-drafts`, `argument-graphs`, `review-comments`):

- **GET /api/v1/research/{entity}**
  - Returns a list of all items for the tenant.

- **POST /api/v1/research/{entity}**
  - Creates a new item.

- **GET /api/v1/research/{entity}/{id}**
  - Retrieves a specific item by ID.

- **PATCH /api/v1/research/{entity}/{id}**
  - Updates an existing item.

- **POST /api/v1/research/{entity}/{id}/validate**
  - Validates the item.

Other endpoints:
- **POST /api/v1/research/ai/analyze**
- **POST /api/v1/research/ai/recommendations**
- **POST /api/v1/research/workflows/execute**
- **GET /api/v1/research/health/contracts**
- **GET /api/v1/research/metrics/summary**

## 4) Services
- Implement CRUD services for each entity.
- Include methods to create, list, get by ID, update, and validate.
- Validation can perform a simple check or logic relevant to the domain.

## 5) Event Emission
- Emits events matching integration-manifest.json upon specific domain actions.
- Add `cc-starter` correctly.

## 6) Frontend
- Next.js 14 App Router
- Implement a basic layout and pages for each module:
  - List View: Table/list view with filters and pagination
  - Detail View: Detail page with timeline/activity
  - Create/Edit Form: Schema validation, toast notifications
  - Error/Loading states
- Tests with Vitest and React Testing Library.
