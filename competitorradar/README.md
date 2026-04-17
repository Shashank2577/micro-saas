# CompetitorRadar

CompetitorRadar is an AI competitive intelligence platform that continuously monitors competitor product updates, pricing changes, hiring signals, customer review sentiment, social activity, and press.

## Features
- Competitor watchlist management
- Change alert system
- AI-generated battlecard creation
- Win/loss analysis dashboard
- Feature comparison matrix

## Setup
Backend (Spring Boot):
`cd backend && mvn clean compile`
`cd backend && mvn test`

Frontend (Next.js):
`cd frontend && npm install`
`cd frontend && npm test`
`cd frontend && npm run build`

## Running Locally
Use Docker Compose to run the full application suite:
`docker compose -f infra/compose.apps.yml up -d`
