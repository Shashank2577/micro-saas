# WealthPlan

An AI-powered financial planning and goal tracking platform. Part of the Nexus Hub micro-saas ecosystem.

## Features
* **Goal definition and prioritization**: Track retirement, home purchases, and custom financial goals.
* **Net worth tracking**: Monitor assets and liabilities over time.
* **Retirement readiness**: Calculate if you're on track using Monte Carlo simulations.
* **Scenario Modeling**: Simulate conservative, moderate, and aggressive market returns.
* **AI-generated recommendations**: Personalized advice based on your current standing and goals using LiteLLM.

## Architecture
- Backend: Spring Boot 3, Java 21, PostgreSQL, Maven
- Frontend: Next.js (App Router), TypeScript, Tailwind CSS, Recharts

## Setup & Running Locally

### Backend
Make sure you have a local PostgreSQL running on port 5432 and LiteLLM running on port 4000.
```
cd backend
mvn spring-boot:run
```
The API runs on port 8202.

### Frontend
```
cd frontend
npm install
npm build
```

## Testing
To run backend tests:
```
cd backend
mvn clean verify
```

To run frontend tests:
```
cd frontend
npm test
```
