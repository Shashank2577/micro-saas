# BrandVoice Detailed Specification

## Overview
BrandVoice is a brand messaging consistency analyzer for content creators and marketing teams.
It helps maintain consistent brand voice across channels.

## Database Schema (PostgreSQL)
- **brand_profiles**: id (UUID), tenant_id (UUID), name (VARCHAR), tone (VARCHAR), vocabulary (JSONB), values (JSONB), templates (JSONB), created_at, updated_at
- **content_audits**: id (UUID), tenant_id (UUID), brand_profile_id (UUID), content_text (TEXT), status (VARCHAR), created_at, updated_at
- **audit_results**: id (UUID), tenant_id (UUID), content_audit_id (UUID), consistency_score (INT), sentiment_alignment (VARCHAR), overall_feedback (TEXT), created_at
- **audit_findings**: id (UUID), tenant_id (UUID), audit_result_id (UUID), original_text (TEXT), suggested_text (TEXT), explanation (TEXT), category (VARCHAR), created_at

## Backend API Endpoints (Spring Boot)

### Brand Profiles
- `POST /api/v1/brand-profiles`: Create a new profile
- `GET /api/v1/brand-profiles`: List profiles for tenant
- `GET /api/v1/brand-profiles/{id}`: Get specific profile
- `PUT /api/v1/brand-profiles/{id}`: Update profile
- `DELETE /api/v1/brand-profiles/{id}`: Delete profile

### Content Audits
- `POST /api/v1/audits`: Submit content for audit (async processing)
- `GET /api/v1/audits`: List past audits
- `GET /api/v1/audits/{id}`: Get specific audit details

### Audit Results & Findings
- `GET /api/v1/audits/{id}/results`: Get results of an audit (includes findings and scores)

### Cross-App API / Integrations
- `POST /api/v1/integrations/consistency-score`: Synchronous endpoint to evaluate content against a brand profile ID and return a consistency score.
- `POST /api/v1/webhooks/external-content`: Webhook endpoint for external systems to submit content for audit.

### PDF Export
- `GET /api/v1/audits/{id}/export/pdf`: Download audit report as PDF.

## Services
- **BrandProfileService**: CRUD operations for brand profiles.
- **ContentAuditService**: Create audit, trigger async processing.
- **AuditProcessingService**: Asynchronously process content audits via pgmq.
- **BrandVoiceAnalysisService**: Interface with LiteLLM to analyze tone, vocabulary, sentiment.
- **BrandVoiceSuggestionService**: Interface with LiteLLM for rewrite suggestions.
- **PdfExportService**: Generate PDF reports using iText.

## Frontend (Next.js)
- `/`: Dashboard showing compliance trends over past audits.
- `/profiles`: Manage brand profiles.
- `/profiles/[id]`: Edit specific profile.
- `/audits`: List of content audits.
- `/audits/new`: Upload content to audit.
- `/audits/[id]`: View audit result, suggestions, and download PDF.

## Acceptance Criteria
- User can create brand profile with guidelines.
- User can upload content and receive consistency audit.
- AI provides rewrite suggestions with explanations.
- Dashboard shows compliance trends.
- Webhook accepts external content.
- Cross-app API evaluates consistency scores.
- Export audit report to PDF.
