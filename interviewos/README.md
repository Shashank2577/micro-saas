# InterviewOS

InterviewOS is an HR Talent application built to handle structured interview orchestration and evaluator consistency scoring. This module implements tenant-isolated domain logic for interview plans, question banks, scorecards, evaluations, and final decision packets.

## Environment Variables

The backend application requires the following environment variables to run:
- \`DB_URL\`: The PostgreSQL database URL (default: \`jdbc:postgresql://localhost:5432/interviewos\`)
- \`DB_USER\`: The PostgreSQL database user (default: \`postgres\`)
- \`DB_PASS\`: The PostgreSQL database password (default: \`postgres\`)
- \`SERVER_PORT\`: The port the backend will run on (default: \`8080\`)

## Setup & Running Locally

### Backend (Spring Boot + Java 21)
1. Ensure Java 21 and Maven are installed.
2. Provide an active Postgres database instance.
3. Run the backend:
   \`\`\`sh
   cd interviewos/backend
   mvn clean spring-boot:run &
   \`\`\`

### Frontend (Next.js)
1. Ensure Node and npm are installed.
2. Run the frontend:
   \`\`\`sh
   cd interviewos/frontend
   npm install
   npm run dev &
   \`\`\`

## Testing

### Backend
Run unit tests for services and controllers:
\`\`\`sh
cd interviewos/backend
mvn clean verify
\`\`\`

### Frontend
Run frontend component tests using Vitest:
\`\`\`sh
cd interviewos/frontend
npm run test
\`\`\`

## Runbook

### Database Migrations
We use Flyway for managing the database schema. Migrations will automatically apply on application startup using the \`baseline-on-migrate: true\` config when connected to Postgres. Migrations are located in \`interviewos/backend/src/main/resources/db/migration/\`.

### Deployment & Docker
A \`Dockerfile\` is provided in the \`interviewos/backend\` folder, which builds a minimal alpine Java 21 container. Build it using:
\`\`\`sh
docker build -t interviewos-backend interviewos/backend
\`\`\`

Run the container:
\`\`\`sh
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://db:5432/interviewos \
  -e DB_USER=your_user \
  -e DB_PASS=your_pass \
  interviewos-backend
\`\`\`
