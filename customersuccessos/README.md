# Customer Success OS

Customer Success OS is an AI-native customer success operations platform that automates QBR generation, real-time health scoring, and expansion opportunity identification.

## Overview
This micro-SaaS application helps Customer Success Managers (CSMs) automate the creation of Quarterly Business Reviews and manages customer accounts seamlessly across the micro-saas ecosystem. It integrates via Litellm to summarize accounts and predict expansion.

## Architecture
- **Backend**: Spring Boot 3.3.5 (Java 21) exposing REST APIs. Uses a PostgreSQL database and integrates with LiteLLM for text generation.
- **Frontend**: Next.js App Router providing dashboards, account lists, and QBR views.

## Running Locally
1. Start infrastructure from root:
   ```
   cd ../infra
   docker-compose up -d postgres redis litellm
   ```
2. Start Backend:
   ```
   cd backend
   mvn spring-boot:run
   ```
3. Start Frontend:
   ```
   cd frontend
   npm run dev &
   ```

## Testing
- Backend tests: `mvn test`
- Frontend tests: `npm test`
