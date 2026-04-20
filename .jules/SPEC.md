# RevOpsAI - Detailed Spec
This document provides the detailed specification for the revopsai implementation as per Phase 1 of the Jules Autonomous Build Protocol.

## Overview
App: revopsai
Port: 8147
Cluster: G
Theme: Sales & Revenue Intelligence

## 1. Entities & Schema
- PipelineMetric (id, tenant_id, segment, stage, count, value, forecast_value, updated_at)
- CacCalculation (id, tenant_id, segment, channel, cohort_month, cac, confidence_interval, created_at, updated_at)
- LtvModel (id, tenant_id, segment, ltv, payback_months, retention_rate, expansion_rate, created_at, updated_at)
- SalesVelocity (id, tenant_id, segment, stage, median_days, p25_days, p75_days, created_at, updated_at)
- ForecastAccuracy (id, tenant_id, period, forecast_amount, actual_amount, variance_percent, created_at, updated_at)
- GtmGap (id, tenant_id, metric_type, plan_value, actual_value, variance, severity, created_at, updated_at)

## 2. API Endpoints
- `GET /api/v1/pipeline-metrics`
- `GET /api/v1/cac-calculations`
- `GET /api/v1/ltv-models`
- `GET /api/v1/sales-velocity`
- `GET /api/v1/forecast-accuracy`
- `GET /api/v1/gtm-gaps`
- `POST /api/v1/nlp/query` (for natural language revenue questions)
- `POST /api/v1/scenarios/what-if`

## 3. Services
- PipelineMetricsService
- CacLtvService
- SalesVelocityService
- NlpRevenueService (LiteLLM integration)
- AnomalyDetectionService
- ForecastingService
- GtmAnalysisService

## 4. Frontend Pages
- `/` (Dashboard)
- `/pipeline` (Pipeline velocity & coverage)
- `/cac-ltv` (CAC/LTV & payback analysis)
- `/forecast` (Forecast accuracy & what-if modeling)
- `/gtm-gaps` (GTM execution gaps)
- `/nlp` (Natural language query interface)

## 5. Integration Manifest
To be generated at `/integration-manifest.json` exactly matching the WORK ORDER.
