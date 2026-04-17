# DocumentVault Detailed Specification

## Overview
DocumentVault is a secure document storage and management system. It handles file uploads, encryption, versioning, access control, OCR, and document lifecycle.

## Database Schema (PostgreSQL)

```sql
-- Flyway migration V1__init.sql

CREATE TABLE documents (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    folder_id UUID,
    current_version_id UUID,
    status VARCHAR(50) NOT NULL, -- ACTIVE, ARCHIVED, DELETED
    retention_hold BOOLEAN DEFAULT FALSE,
    expiration_date TIMESTAMP,
    owner_id UUID NOT NULL,
    size_bytes BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    encryption_key_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by UUID,
    updated_by UUID
);
CREATE INDEX idx_documents_tenant_id ON documents(tenant_id);

CREATE TABLE document_versions (
    id UUID PRIMARY KEY,
    document_id UUID NOT NULL REFERENCES documents(id),
    tenant_id UUID NOT NULL,
    version_number INT NOT NULL,
    s3_key VARCHAR(1024) NOT NULL,
    size_bytes BIGINT NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    ocr_text TEXT,
    created_at TIMESTAMP NOT NULL,
    created_by UUID
);
CREATE INDEX idx_document_versions_tenant_id ON document_versions(tenant_id);

CREATE TABLE document_shares (
    id UUID PRIMARY KEY,
    document_id UUID NOT NULL REFERENCES documents(id),
    tenant_id UUID NOT NULL,
    shared_with_email VARCHAR(255) NOT NULL,
    access_level VARCHAR(50) NOT NULL, -- VIEW, EDIT
    password_hash VARCHAR(255),
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    created_by UUID
);
CREATE INDEX idx_document_shares_tenant_id ON document_shares(tenant_id);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    document_id UUID REFERENCES documents(id),
    user_id UUID NOT NULL,
    action VARCHAR(100) NOT NULL, -- UPLOAD, VIEW, DOWNLOAD, DELETE, SHARE, VERSION_ROLLBACK
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_audit_logs_tenant_id ON audit_logs(tenant_id);

CREATE TABLE storage_quotas (
    tenant_id UUID PRIMARY KEY,
    total_quota_bytes BIGINT NOT NULL,
    used_bytes BIGINT NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## REST API Endpoints

### Documents
- `POST /api/documents/upload` (Multipart form-data)
  - Request: `file`, `title`, `description`, `folder_id`
  - Response: Document DTO
  - Action: Uploads file, scans for virus (simulated), encrypts, stores in S3, triggers OCR (LiteLLM simulation)
- `GET /api/documents`
  - Query params: `search`, `folderId`, `status`, `page`, `size`
  - Response: Page of Document DTOs
- `GET /api/documents/{id}`
  - Response: Document Details DTO including current version and share info
- `PUT /api/documents/{id}`
  - Request: `title`, `description`, `status`, `retentionHold`
  - Response: Document DTO
- `DELETE /api/documents/{id}`
  - Action: Marks as deleted or permanently deletes based on retention hold

### Versions
- `GET /api/documents/{id}/versions`
  - Response: List of Document Version DTOs
- `POST /api/documents/{id}/versions` (Multipart form-data)
  - Request: `file`
  - Response: Document Version DTO
- `POST /api/documents/{id}/versions/{versionId}/rollback`
  - Response: Document DTO

### Preview & Download
- `GET /api/documents/{id}/preview`
  - Response: Bytes or presigned URL for inline viewing
- `GET /api/documents/{id}/download`
  - Response: File download

### Batch Operations
- `POST /api/documents/batch/download`
  - Request: `List<UUID> documentIds`
  - Response: ZIP file download

### Sharing
- `POST /api/documents/{id}/share`
  - Request: `email`, `accessLevel`, `expiresAt`, `password`
  - Response: Share Link DTO
- `DELETE /api/documents/{id}/share/{shareId}`

### Audit & Quota
- `GET /api/documents/{id}/audit`
  - Response: List of Audit Log DTOs
- `GET /api/quota`
  - Response: Quota DTO (used, total, remaining)

## Service Methods

### DocumentService
- `uploadDocument(MultipartFile file, DocumentCreateRequest req, UUID userId, UUID tenantId)`
- `getDocument(UUID id, UUID tenantId)`
- `searchDocuments(String query, UUID tenantId, Pageable pageable)`
- `updateDocument(UUID id, DocumentUpdateRequest req, UUID tenantId)`
- `deleteDocument(UUID id, UUID tenantId)`
- `previewDocument(UUID id, UUID tenantId)`
- `downloadDocument(UUID id, UUID tenantId)`

### DocumentVersionService
- `addVersion(UUID documentId, MultipartFile file, UUID userId, UUID tenantId)`
- `rollbackToVersion(UUID documentId, UUID versionId, UUID tenantId)`

### ShareService
- `shareDocument(UUID documentId, ShareRequest req, UUID tenantId)`
- `revokeShare(UUID documentId, UUID shareId, UUID tenantId)`

### AuditService
- `logAction(UUID tenantId, UUID documentId, UUID userId, String action, String ip, String userAgent)`

### QuotaService
- `checkQuota(UUID tenantId, long sizeBytes)`
- `updateUsage(UUID tenantId, long deltaBytes)`

## React Components (Next.js)

### Pages
- `/` - Dashboard, Document Explorer, Search, Quota widget
- `/document/[id]` - Document Details, Preview, Versions, Audit Logs, Share settings

### Components
- `DocumentList` - Table view of documents
- `DocumentUpload` - Drag & drop upload area with progress
- `DocumentPreview` - Inline PDF/Image viewer
- `VersionHistory` - List of versions with rollback button
- `ShareDialog` - Dialog to create share links
- `QuotaWidget` - Progress bar showing used vs total storage
- `AuditTrail` - List of recent activities

## AI Integration Points
- LiteLLM is used to simulate OCR and document classification upon upload.
- Prompt: "Analyze the following document text and provide searchable keywords and classification."

## Error Handling
- OverQuotaException -> 400 Bad Request
- VirusDetectedException -> 400 Bad Request
- DocumentNotFoundException -> 404 Not Found
- RetentionHoldException -> 403 Forbidden

## Edge Cases and Validations
- Retention hold prevents deletion.
- Expiration automatically removes documents via scheduled job.
- Uploads exceeding quota are blocked early.
