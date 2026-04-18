# NonprofitOS Detailed Specification

## 1. Overview
NonprofitOS is an AI-powered operations platform for nonprofits and foundations. It automates grant research and writing, provides donor relationship intelligence, and streamlines impact measurement.

## 2. Database Schema
```sql
CREATE TABLE donors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    total_given NUMERIC(15, 2) DEFAULT 0.0,
    engagement_score INTEGER DEFAULT 0,
    upgrade_potential VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE grants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    funder VARCHAR(255) NOT NULL,
    amount NUMERIC(15, 2),
    deadline TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL,
    draft_content TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE impacts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    metric_value NUMERIC(15, 2) NOT NULL,
    narrative TEXT,
    date_recorded TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

## 3. REST API Endpoints

### Donors
- `GET /api/donors`
  - Returns list of donors.
- `POST /api/donors`
  - Creates a new donor.
- `GET /api/donors/{id}/intelligence`
  - Generates AI insights on donor engagement and upgrade potential.

### Grants
- `GET /api/grants`
  - Returns list of grants.
- `POST /api/grants`
  - Creates a new grant tracking entry.
- `POST /api/grants/{id}/generate-draft`
  - Uses AI to generate a grant proposal draft based on donor history and impact metrics.

### Impacts
- `GET /api/impacts`
  - Returns list of impact metrics.
- `POST /api/impacts`
  - Records a new impact metric.
- `POST /api/impacts/generate-narrative`
  - Generates a narrative report based on recent impacts.

## 4. Service Methods
- `DonorService.calculateIntelligence(UUID donorId)`
  - Calls LiteLLM to assess donor upgrade potential.
- `GrantService.generateDraft(UUID grantId)`
  - Uses LiteLLM to draft a proposal.
- `ImpactService.generateNarrative()`
  - Uses LiteLLM to summarize impact data into a narrative.

## 5. React Components
- `DonorList`: Displays donors and their engagement scores.
- `DonorIntelligenceCard`: Shows AI-generated insights for a specific donor.
- `GrantTracker`: Kanban board or list of grants by status.
- `GrantDraftGenerator`: UI to trigger and edit AI-generated grant drafts.
- `ImpactDashboard`: Charts showing metrics and AI-generated narrative summaries.

## 6. AI Integration Points (LiteLLM)
- **Donor Intelligence:** Analyzes giving history to predict future giving capacity.
- **Grant Writing:** Generates text tailored to the funder's stated goals.
- **Impact Narrative:** Turns quantitative data into compelling stories for newsletters.

## 7. Error Handling
- Validates all requests (400 Bad Request).
- Returns 404 for missing resources.
- Returns 503 if AI service is unavailable.
- Uses Circuit Breaker for LiteLLM calls.

## 8. Acceptance Criteria
- End-to-end tests for CRUD operations on Donors, Grants, and Impacts.
- Service unit tests for AI integration methods mocking LiteLLM.
- Frontend components render correctly with mocked API data.
