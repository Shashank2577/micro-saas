# Contract Portfolio

A legal intelligence module within the ecosystem for contract extraction, risk indexing, and renewal intelligence.

## Features
- **Contract Records:** Store and manage full contract lifecycles.
- **Clause Extraction:** Identify and structure key clauses using AI.
- **Obligations:** Track deliverables and payments over time.
- **Risk Scores:** Automated scoring matrix for counterparty risk.
- **Renewals:** Proactive alerts for upcoming expiration dates.
- **Counterparties:** Unified view of entities engaged in contracts.

## Technical Stack
- **Backend:** Java 21, Spring Boot 3, PostgreSQL, Flyway, Maven
- **Frontend:** Next.js 15, TypeScript, Tailwind CSS
- **AI Integration:** LiteLLM wrapper for LLM requests.

## Running Locally
### Backend
Make sure you have `cc-starter` installed in your local maven repository.
`mvn spring-boot:run`

### Frontend
`npm install`
`npm run dev`

## Docker
`docker build -t contractportfolio-backend .`
`docker run -p 8150:8150 contractportfolio-backend`

## API Documentation
Once running, OpenAPI documentation is available at `http://localhost:8150/swagger-ui.html`.
