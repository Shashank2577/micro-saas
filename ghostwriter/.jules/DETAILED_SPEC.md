# GhostWriter Detailed Specification

## Overview
GhostWriter is an AI-powered content writing assistant. It generates blog posts, social media content, email copy, and marketing materials using AI.

## Features
- Multi-format content generation
- Tone/style control
- Brand voice consistency
- Content history
- Export to multiple formats

## Database Schema (PostgreSQL)

### `documents` table
- `id` (UUID, Primary Key)
- `tenant_id` (UUID, Not Null)
- `title` (VARCHAR(255))
- `content` (TEXT)
- `format` (VARCHAR(50)) - enum: BLOG_POST, SOCIAL_MEDIA, EMAIL, MARKETING
- `tone` (VARCHAR(50)) - enum: PROFESSIONAL, CASUAL, ENTHUSIASTIC, PERSUASIVE
- `status` (VARCHAR(50)) - enum: DRAFT, GENERATING, COMPLETED, FAILED
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## REST API Endpoints

### Documents API (`/api/documents`)
- `GET /api/documents`
  - Fetch all documents for the current tenant.
  - Query params: `format` (optional)
  - Response: `List<DocumentResponse>`
- `GET /api/documents/{id}`
  - Fetch a single document by ID.
  - Response: `DocumentResponse`
- `POST /api/documents/generate`
  - Request AI generation of a new document.
  - Request Body: `GenerateRequest` (format, tone, topic, instructions)
  - Response: `DocumentResponse` (status GENERATING)
- `PUT /api/documents/{id}`
  - Update a document manually.
  - Request Body: `DocumentUpdateRequest` (title, content)
  - Response: `DocumentResponse`
- `DELETE /api/documents/{id}`
  - Delete a document.
  - Response: 204 No Content

## Services

### `DocumentService`
- `List<Document> getAllDocuments(UUID tenantId, String format)`
- `Document getDocumentById(UUID id, UUID tenantId)`
- `Document generateDocument(UUID tenantId, GenerateRequest request)` - Async processing? Or sync but blocking? We'll do synchronous for simplicity.
- `Document updateDocument(UUID id, UUID tenantId, DocumentUpdateRequest request)`
- `void deleteDocument(UUID id, UUID tenantId)`

### `AiGenerationService` (LiteLLM Client)
- Integrates with the cross-cutting AI Gateway.
- `String generateContent(String format, String tone, String topic, String instructions)`

## Frontend (Next.js)

### Pages
- `/` - Dashboard: List of recent documents, quick action to create new.
- `/documents` - Full list of content history.
- `/generate` - Form to create new content (select format, tone, enter topic).
- `/documents/[id]` - Editor view to see generated content, edit it, and export.

### Components
- `DocumentList`
- `GenerateForm`
- `DocumentEditor`
- `Layout`, `Header`, `Sidebar`

## External Integrations
- `emits`: `ContentGeneratedEvent`
- `consumes`: `BrandVoiceUpdatedEvent` (Optional, we'll keep it simple if not specified)

## Acceptance Criteria
1. User can view all previously generated documents.
2. User can request generation of a blog post, social media post, email, or marketing copy.
3. User can specify tone (Professional, Casual, etc.).
4. Generated content is saved to the database.
5. User can edit the generated content.
6. User can delete a document.
