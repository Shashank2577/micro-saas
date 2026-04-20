# HealthcareDocAI

HealthcareDocAI is an AI clinical documentation assistant designed to generate SOAP notes, referral letters, and clinical summaries from voice-to-text transcriptions of clinical encounters.

## Features
- Voice-to-text transcription of clinical encounters
- SOAP note, referral letter, and clinical summary generation from transcripts
- EHR integration (Epic, Cerner)
- HIPAA compliance by design
- Clinician override capability
- Template customization per specialty

## Tech Stack
- **Backend:** Spring Boot 3.3.5 (Java 21), PostgreSQL 16 (HIPAA-compliant)
- **Frontend:** Next.js 15, TypeScript, Tailwind CSS
- **Integrations:** Google Cloud Speech-to-Text (medical model), Anthropic API (Claude), EHR SDKs
- **Ecosystem:** `cc-starter` (shared foundation stack)

## Running Locally

### Backend
1. Ensure the shared `cc-starter` foundation stack is installed:
   ```bash
   mvn -f ../cross-cutting/pom.xml install -DskipTests
   ```
2. Navigate to the backend directory:
   ```bash
   cd healthcaredocai/backend
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The application runs on `localhost:8150` by default.

### Frontend
1. Navigate to the frontend directory:
   ```bash
   cd healthcaredocai/frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```

## Testing

### Backend
To run backend tests:
```bash
mvn -f healthcaredocai/backend/pom.xml test
```

### Frontend
To run frontend component tests:
```bash
npm --prefix healthcaredocai/frontend test
```

## API Documentation
The OpenAPI documentation is available when the backend is running at:
`http://localhost:8150/swagger-ui.html`
