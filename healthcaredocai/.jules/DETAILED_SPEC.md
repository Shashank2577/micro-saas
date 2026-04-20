# Detailed Specification for HealthcareDocAI

## 1. Overview
HealthcareDocAI is an AI clinical documentation assistant designed to generate SOAP notes, referral letters, and clinical summaries from voice-to-text transcriptions of clinical encounters. This application falls under Cluster K (Healthcare) and is a critical tool for medical professionals to streamline their workflow. It leverages AI models (like Google Cloud Speech-to-Text for medical and Anthropic Claude for text generation) to reduce administrative overhead while ensuring strict HIPAA compliance.

## 2. Architecture & Tech Stack
- **Backend**: Spring Boot 3.3.5 (Java 21)
- **Database**: PostgreSQL 16 (HIPAA-compliant)
- **Frontend**: Next.js 15 with TypeScript
- **Integrations**: Google Cloud Speech API, Anthropic API (Claude), EHR APIs (Epic Fhir, Cerner)
- **Cross-Cutting Modules**:
  - storage: HIPAA-compliant S3
  - audit: medical records audit logging
  - ai: Claude for clinical documentation
- **Deployment**: Local testing port 8150.

## 3. Domain Model
### ClinicalEncounter
- id (UUID)
- tenantId (UUID)
- patientId (String)
- clinicianId (String)
- transcript (Text)
- timestamp (OffsetDateTime)

### GeneratedNote
- id (UUID)
- tenantId (UUID)
- encounterId (UUID)
- noteType (Enum: SOAP, REFERRAL, SUMMARY)
- content (Text)
- status (Enum: DRAFT, REVIEWED, APPROVED)
- createdAt (OffsetDateTime)
- updatedAt (OffsetDateTime)

### ClinicalTemplate
- id (UUID)
- tenantId (UUID)
- specialty (String)
- noteType (Enum: SOAP, REFERRAL, SUMMARY)
- templateFormat (Text)

## 4. API Endpoints
### External/Internal
- `POST /api/v1/encounters/transcribe` - Accepts audio file or text, returns transcription and creates a ClinicalEncounter.
- `POST /api/v1/notes/generate` - Generates a note from an encounter using AI.
- `GET /api/v1/templates/{specialty}` - Retrieves templates for a specialty.
- `POST /api/v1/ehr/epic/sync` - Syncs an approved note to an EHR.
- `GET /api/v1/notes/{id}` - Retrieves a note.
- `PUT /api/v1/notes/{id}/status` - Updates note status (e.g., clinician approval).
- `PUT /api/v1/notes/{id}` - Clinician override of note content.

## 5. Security & Compliance
- **HIPAA Compliance**: Mandatory for all layers. PII/PHI must be encrypted at rest and in transit.
- **Audit Logging**: All access to encounters and notes, generation events, and manual overrides must be strictly logged with user and timestamp.
- **Multi-tenant**: Every record must include `tenantId`. Services and repositories must enforce tenant scoping.

## 6. Implementation Plan
### Phase 2: Full Implementation
**Backend:**
1. Scaffold Spring Boot application in `healthcaredocai/backend`.
2. Add JPA Entities: `ClinicalEncounter`, `GeneratedNote`, `ClinicalTemplate`.
3. Add Repositories with tenant scoping.
4. Implement Services: `TranscriptionService` (mock Google Speech or use provided AI module), `NoteGenerationService` (using LiteLLM/Claude), `EHRIntegrationService` (mock integrations), `TemplateService`.
5. Implement REST Controllers.
6. Write unit and integration tests to ensure >80% coverage.

**Frontend:**
1. Scaffold Next.js application in `healthcaredocai/frontend`.
2. Create pages for:
   - Voice recorder & encounter list
   - Note editor (review/override)
   - EHR sync dashboard
3. Implement API integration hooks.
4. Write tests for key components.

### Phase 3: Testing & Validation
- Run backend tests: `mvn test`
- Run frontend tests: `npm test`
- Verify metrics and latency requirements (e.g. mock note generation in <30 seconds).

### Phase 4: Submission
- Commit code and push.
