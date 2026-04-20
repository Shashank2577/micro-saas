# HealthcareDocAI Specification Understanding

The goal is to implement an AI clinical documentation assistant called HealthcareDocAI. The application is built using the ecosystem's hub-and-spoke architecture, relying on `cc-starter` for backend shared functionalities, and Next.js for the frontend.

## Key Requirements:
- Voice-to-text transcription.
- SOAP note, Referral letter, and Clinical summary generation.
- HIPAA compliance.
- EHR Integrations (Epic, Cerner).

## Architecture
- **Backend**: Spring Boot 3.3.5 (Java 21). Uses Postgres (HIPAA-compliant) and Flyway.
- **Frontend**: Next.js 15.

## Findings
The application was partially scaffolded. The backend contains the domain entities (`ClinicalEncounter`, `GeneratedNote`, `ClinicalTemplate`), services (`TranscriptionService`, `NoteGenerationService`, `EHRIntegrationService`), and tests. The frontend contains pages for `notes`, `recorder`, `ehr-sync`, and tests.

I am responsible for fixing any existing issues in tests (such as the `act(...)` warning in frontend tests), updating the `integration-manifest.json`, the `README.md`, and documenting the phases of the autonomous build.
