# CashflowAnalyzer

## Purpose
CashflowAnalyzer provides historical cashflow diagnostics, forecasting, and anomaly explanations for finance analysts, CFOs, and controllers.

## API Contracts
- `/api/v1/cashflow/cashflow-periods`
- `/api/v1/cashflow/cash-movements`
- `/api/v1/cashflow/trend-signals`
- `/api/v1/cashflow/forecast-runs`
- `/api/v1/cashflow/anomaly-flags`
- `/api/v1/cashflow/ai/analyze`
- `/api/v1/cashflow/ai/recommendations`
- `/api/v1/cashflow/workflows/execute`
- `/api/v1/cashflow/health/contracts`

## Setup & Running Locally
Backend:
`mvn -pl cashflowanalyzer/backend spring-boot:run`
Frontend:
`npm --prefix cashflowanalyzer/frontend run dev`

## Event Integrations
**Emits:**
- cashflowanalyzer.forecast.generated
- cashflowanalyzer.anomaly.detected
- cashflowanalyzer.insight.published

**Consumes:**
- invoiceprocessor.invoice.approved
- budgetpilot.reforecast.completed
- financenarrator.summary.requested
