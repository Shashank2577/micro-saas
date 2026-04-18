# ChurnPredictor

An AI churn prevention platform that predicts which customers will churn 30-90 days in advance using usage patterns, support sentiment, engagement trends, NPS responses, and health scores.

## Stack
- Backend: Spring Boot 3.3.5, PostgreSQL, pgvector
- Frontend: Next.js 14, TypeScript, Tailwind CSS, Recharts

## Running locally
Use `docker-compose up -d` in the root infra folder.

## Testing
- Backend: `mvn -pl churnpredictor/backend clean verify`
- Frontend: `npm --prefix churnpredictor/frontend test`
