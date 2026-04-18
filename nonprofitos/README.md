# NonprofitOS

NonprofitOS is an AI-powered operations platform for nonprofits and foundations. It automates grant research and writing, provides donor relationship intelligence, and streamlines impact measurement.

## Architecture
- **Backend:** Spring Boot (Java 21) with `cc-starter` tenancy framework. PostgreSQL DB, Flyway migrations, LiteLLM integration.
- **Frontend:** Next.js (App Router), TypeScript, TailwindCSS.

## Requirements
- Docker & Docker Compose
- Maven
- Node.js 20+

## Running Locally

1. Start infrastructure:
   cd infra && docker-compose up -d

2. Start Backend:
   cd nonprofitos/backend
   mvn spring-boot:run &

   The backend will be available at http://localhost:8080.
   Swagger UI: http://localhost:8080/swagger-ui.html

3. Start Frontend:
   cd nonprofitos/frontend
   npm install
   npm run build && npm start &

   The frontend will be available at http://localhost:3000.

## Testing

Run backend tests:
mvn -pl nonprofitos/backend clean verify

Run frontend tests:
npm --prefix nonprofitos/frontend test
