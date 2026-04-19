# CopyOptimizer Detailed Specification

## Domain Models (Database Schema & Entities)

### `copy_assets`
- `id`: UUID PRIMARY KEY
- `tenant_id`: UUID NOT NULL
- `name`: VARCHAR(180) NOT NULL
- `status`: VARCHAR(40) NOT NULL (e.g., DRAFT, PUBLISHED)
- `metadata_json`: JSONB
- `created_at`: TIMESTAMPTZ NOT NULL
- `updated_at`: TIMESTAMPTZ NOT NULL

### `copy_variants` (Overrides `V1__init.sql` schema if needed to match Domain Model)
- `id`: UUID PRIMARY KEY
- `tenant_id`: UUID NOT NULL
- `name`: VARCHAR(180) NOT NULL
- `status`: VARCHAR(40) NOT NULL (e.g., GENERATED, PROMOTED)
- `metadata_json`: JSONB
- `created_at`: TIMESTAMPTZ NOT NULL
- `updated_at`: TIMESTAMPTZ NOT NULL
- *Note*: We will add a foreign key to `copy_assets(id)` via `metadata_json` or a direct column for association, although the spec domain model only mentions basic fields. We will use `metadata_json` to store text, cta, prediction metrics etc., or expand the model slightly to be practical for the API.

### `audience_segments`
- `id`: UUID PRIMARY KEY
- `tenant_id`: UUID NOT NULL
- `name`: VARCHAR(180) NOT NULL
- `status`: VARCHAR(40) NOT NULL
- `metadata_json`: JSONB
- `created_at`: TIMESTAMPTZ NOT NULL
- `updated_at`: TIMESTAMPTZ NOT NULL

### `prediction_scores`
- `id`: UUID PRIMARY KEY
- `tenant_id`: UUID NOT NULL
- `name`: VARCHAR(180) NOT NULL
- `status`: VARCHAR(40) NOT NULL
- `metadata_json`: JSONB
- `created_at`: TIMESTAMPTZ NOT NULL
- `updated_at`: TIMESTAMPTZ NOT NULL

### `experiment_plans`
- `id`: UUID PRIMARY KEY
- `tenant_id`: UUID NOT NULL
- `name`: VARCHAR(180) NOT NULL
- `status`: VARCHAR(40) NOT NULL
- `metadata_json`: JSONB
- `created_at`: TIMESTAMPTZ NOT NULL
- `updated_at`: TIMESTAMPTZ NOT NULL

### `winning_variants`
- `id`: UUID PRIMARY KEY
- `tenant_id`: UUID NOT NULL
- `name`: VARCHAR(180) NOT NULL
- `status`: VARCHAR(40) NOT NULL
- `metadata_json`: JSONB
- `created_at`: TIMESTAMPTZ NOT NULL
- `updated_at`: TIMESTAMPTZ NOT NULL

## API Endpoints

All endpoints require `X-Tenant-ID` header.

- **Assets:**
  - `GET /api/v1/copy/copy-assets`
  - `POST /api/v1/copy/copy-assets`
  - `GET /api/v1/copy/copy-assets/{id}`
  - `PATCH /api/v1/copy/copy-assets/{id}`
  - `POST /api/v1/copy/copy-assets/{id}/validate`
- **Variants:**
  - `GET /api/v1/copy/variants`
  - `POST /api/v1/copy/variants`
  - `GET /api/v1/copy/variants/{id}`
  - `PATCH /api/v1/copy/variants/{id}`
  - `POST /api/v1/copy/variants/{id}/validate`
- **Scoring (PredictionScores):**
  - `GET /api/v1/copy/prediction-scores`
  - `POST /api/v1/copy/prediction-scores`
  - `GET /api/v1/copy/prediction-scores/{id}`
  - `PATCH /api/v1/copy/prediction-scores/{id}`
  - `POST /api/v1/copy/prediction-scores/{id}/validate`
- **Experiments (ExperimentPlans):**
  - `GET /api/v1/copy/experiment-plans`
  - `POST /api/v1/copy/experiment-plans`
  - `GET /api/v1/copy/experiment-plans/{id}`
  - `PATCH /api/v1/copy/experiment-plans/{id}`
  - `POST /api/v1/copy/experiment-plans/{id}/validate`
- **Promotion (WinningVariants):**
  *(Not explicitly listed in section 5 API contract, but implied by domain model & section 6 & section 3. We will implement standard CRUD under `/api/v1/copy/winning-variants`)*
- **Audience Segments:**
  - `GET /api/v1/copy/audience-segments`
  - `POST /api/v1/copy/audience-segments`
  - `GET /api/v1/copy/audience-segments/{id}`
  - `PATCH /api/v1/copy/audience-segments/{id}`
  - `POST /api/v1/copy/audience-segments/{id}/validate`
- **AI & Analytics:**
  - `POST /api/v1/copy/ai/analyze`
  - `POST /api/v1/copy/ai/recommendations`
  - `POST /api/v1/copy/workflows/execute`
  - `GET /api/v1/copy/health/contracts`
  - `GET /api/v1/copy/metrics/summary`

## Backend Services
- Interfaces with methods: `create`, `update`, `list`, `getById`, `delete`, `validate`, `simulate`
- AI Service wrapping LiteLLM.

## Frontend Next.js Pages
- `/assets`: List/table view of CopyAssets
- `/assets/[id]`: Detail view, timeline, edit form
- `/variants`: List/table view of Variants
- `/variants/[id]`: Detail view
- `/experiments`: List view
- `/scoring`: List view of scores
- `/promotion`: List view of winning variants
- `/analytics`: Dashboard

## Integration Manifest
- Emits: `copyoptimizer.variant.generated`, `copyoptimizer.variant.promoted`, `copyoptimizer.score.computed`
- Consumes: `seointelligence.cluster.generated`, `brandvoice.style.updated`, `contentos.asset.created`
