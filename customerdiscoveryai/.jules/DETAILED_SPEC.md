# CustomerDiscoveryAI - Detailed Specification

## Overview
CustomerDiscoveryAI is an AI user research platform that allows product teams to conduct async interviews at scale, synthesize findings from hundreds of conversations automatically, and generate research reports with confidence levels and supporting quotes.

## Database Schema (PostgreSQL)

### `research_projects`
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `name` (VARCHAR)
- `description` (TEXT)
- `target_audience` (VARCHAR)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### `interviews`
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `project_id` (UUID, foreign key to research_projects)
- `participant_name` (VARCHAR)
- `participant_email` (VARCHAR)
- `status` (VARCHAR: PENDING, IN_PROGRESS, COMPLETED)
- `transcript` (TEXT)
- `summary` (TEXT)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### `insights`
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `project_id` (UUID, foreign key to research_projects)
- `theme` (VARCHAR)
- `description` (TEXT)
- `confidence_score` (DECIMAL)
- `supporting_quotes` (JSONB)
- `created_at` (TIMESTAMP)

### `reports`
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `project_id` (UUID, foreign key to research_projects)
- `title` (VARCHAR)
- `content` (TEXT)
- `status` (VARCHAR: GENERATING, COMPLETED, FAILED)
- `created_at` (TIMESTAMP)

## REST API Endpoints

### Projects
- `GET /api/projects` - List all projects
- `POST /api/projects` - Create a new project
- `GET /api/projects/{id}` - Get project details

### Interviews
- `GET /api/projects/{projectId}/interviews` - List interviews for a project
- `POST /api/projects/{projectId}/interviews` - Add a participant/interview to project
- `POST /api/interviews/{id}/submit-transcript` - Submit the conversation transcript (async interview simulation)

### Insights & Synthesis
- `POST /api/projects/{projectId}/synthesize` - Trigger AI pipeline to generate insights
- `GET /api/projects/{projectId}/insights` - Get generated insights

### Reports
- `POST /api/projects/{projectId}/reports` - Generate a research report
- `GET /api/projects/{projectId}/reports` - List reports
- `GET /api/reports/{id}` - View report

## React Components (Next.js)
- `ProjectList` - Displays all research projects
- `ProjectDetails` - Tabs for Interviews, Insights, Reports
- `InterviewList` - List of participants and statuses
- `InsightCard` - Displays a synthesized insight with confidence level and quotes
- `ReportViewer` - Displays markdown generated research reports

## AI Integration (LiteLLM)
- Synthesis Pipeline: Takes all completed interview transcripts in a project, prompts the AI to identify themes, contradictions, and extract quotes.
- Report Generation Pipeline: Takes insights and prompts AI to write a final research report.

## Event Integration
- Emits: `interview.completed`, `insights.generated`, `report.completed`
- Consumes: `user.created`
