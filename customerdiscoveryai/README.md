# CustomerDiscoveryAI

AI user research platform that automatically synthesizes findings from 100+ interviews.

## Overview
Conducts async video/text interviews at scale using AI interviewers that probe intelligently. Synthesizes findings automatically, identifies themes, and generates research reports with confidence levels.

## Setup

### Backend
1. Ensure PostgreSQL is running.
2. Ensure LiteLLM gateway is running.
3. Start the backend:
```bash
cd backend
mvn spring-boot:run
```

### Frontend
1. Install dependencies: `npm install`
2. Start Next.js dev server: `npm run dev`

## Testing
Run `mvn test` in `backend/` and `npm test` in `frontend/`.
