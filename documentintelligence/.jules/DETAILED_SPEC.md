# Document Intelligence Detailed Specification

## 1. Overview
DocumentIntelligence is an AI document processing platform. It ingests various document types, extracts structured data using OCR and LLMs, routes them, allows semantic search, and supports question answering over the document context.

## 2. Database Schema (PostgreSQL 16 with pgvector)

```sql
-- V1__init.sql

CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE documents (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    storage_path VARCHAR(1024) NOT NULL,
    status VARCHAR(50) NOT NULL, -- UPLOADED, EXTRACTING, ROUTING, COMPLETE, ERROR
    document_type VARCHAR(100), -- AUTO-CLASSIFIED TYPE
    size_bytes BIGINT NOT NULL,
    uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255)
);
CREATE INDEX idx_documents_tenant ON documents(tenant_id);

CREATE TABLE document_extractions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    document_id UUID REFERENCES documents(id) ON DELETE CASCADE,
    extraction_type VARCHAR(100) NOT NULL, -- e.g., FORM_FIELD, TABLE, KEY_VALUE
    key_name VARCHAR(255) NOT NULL,
    value_text TEXT,
    confidence_score DECIMAL(5,4),
    page_number INTEGER,
    bounding_box JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doc_extractions_tenant_doc ON document_extractions(tenant_id, document_id);

CREATE TABLE document_chunks (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    document_id UUID REFERENCES documents(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    page_number INTEGER,
    embedding vector(1536), -- Assuming LiteLLM/OpenAI compatible embeddings
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doc_chunks_tenant_doc ON document_chunks(tenant_id, document_id);

CREATE TABLE document_audit_trails (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    document_id UUID REFERENCES documents(id) ON DELETE CASCADE,
    action VARCHAR(100) NOT NULL,
    details JSONB,
    performed_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doc_audit_tenant_doc ON document_audit_trails(tenant_id, document_id);
```

## 3. REST API Endpoints

### 3.1 Document Management
- **POST /api/documents/upload**
  - Consumes: `multipart/form-data`
  - Returns: `DocumentDTO`
- **GET /api/documents**
  - Returns: `Page<DocumentDTO>`
- **GET /api/documents/{id}**
  - Returns: `DocumentDTO` with status.
- **GET /api/documents/{id}/extractions**
  - Returns: `List<DocumentExtractionDTO>`
- **GET /api/documents/{id}/content**
  - Returns: Presigned URL or raw bytes of document for preview.

### 3.2 Semantic Search & QA
- **POST /api/documents/search**
  - Body: `{ "query": "string", "limit": int }`
  - Returns: `List<DocumentChunkDTO>` (with relevancy scores)
- **POST /api/documents/{id}/qa**
  - Body: `{ "question": "string" }`
  - Returns: `{ "answer": "string", "sources": [...] }`

## 4. Backend Services
- `DocumentService`: Handles file upload to MinIO, tracks status, saves to DB.
- `ExtractionService`: Orchestrates LiteLLM extraction, parses structured JSON outputs from LLM.
- `OCRService`: Calls Tesseract to convert images/scanned PDFs to text.
- `EmbeddingService`: Generates vector embeddings for document chunks using LiteLLM.
- `VectorSearchService`: Queries pgvector for similar chunks.
- `QAService`: Given a question and context chunks, asks LiteLLM to answer.

## 5. Frontend React Components
- `DocumentDashboard`: Main view showing list of documents and status.
- `DocumentUploader`: Drag-and-drop zone using `react-dropzone`.
- `DocumentViewer`: Uses `pdf.js` for previewing.
- `ExtractionViewer`: Sidebar showing extracted key-value pairs and confidence scores.
- `DocumentQA`: Chat-like interface for asking questions about a specific document.
- `SemanticSearch`: Global search bar to query across all uploaded documents.

## 6. Integrations
- LiteLLM: Used for both ChatCompletion (extraction, QA) and Embeddings.
- MinIO (S3): Object storage for documents.
- PostgreSQL + pgvector: RDBMS and Vector Store.
- Tesseract OCR: for image-to-text.

## 7. Assumptions & Tradeoffs
- Tesseract will be executed via `ProcessBuilder` by having the CLI installed, or we'll mock its extraction in the backend if CLI is unavailable in the environment. For the sake of local testing, we will mock Tesseract if the binary is missing.
- Embeddings use a standard 1536-dim dimension assumption, corresponding to standard text-embedding models typically routed via LiteLLM.
- pgmq is stated in backend stack, so document extraction will be triggered asynchronously by pushing a message to a queue after upload. We will use a basic in-memory Spring async abstraction or mock if pgmq is not natively available in the standard local setup, but we'll include pgmq dependency if we can. Given cc-starter often uses Spring Boot's standard queue support, we will implement Spring Events with `@Async` for simplicity of autonomous implementation if pgmq extension is complex to manage locally.
