# FinanceNarrator

FinanceNarrator is a micro-saas ecosystem application designed to generate executive-grade financial narratives from structured financial datasets.

## Requirements
- Java 21
- Node.js 20+
- PostgreSQL
- Maven

## Setup
Backend:
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Frontend:
```bash
cd frontend
npm install
npm run dev &
```

## Contracts and SLOs
- Consumes: `cashflowanalyzer.insight.published`, `budgetpilot.variance.alerted`, `equityintelligence.round.updated`
- Emits: `financenarrator.narrative.generated`, `financenarrator.review.requested`, `financenarrator.export.completed`
- SLOs: 99.9% uptime, latency <300ms for read endpoints.

## Testing
Run backend tests:
```bash
mvn test
```
Run frontend tests:
```bash
npm test
```
