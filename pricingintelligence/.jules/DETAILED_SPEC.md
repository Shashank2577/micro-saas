# DETAILED SPEC: PricingIntelligence

## Architecture Details

PricingIntelligence uses a 6-layer architecture based on `cc-starter`:
1. Entities
2. Repositories
3. Services
4. AI Component
5. REST API Controller
6. Frontend Next.js

## 1. Entities

All entities are tenant-scoped (include `@TenantId` or standard tenancy conventions).

- **PricingExperiment**
  - id (UUID)
  - name (String)
  - startDate (LocalDate)
  - endDate (LocalDate)
  - status (String - PENDING, RUNNING, COMPLETED)
  - variants (JSONB/List)
  - results (String/JSONB)

- **ConversionRecord**
  - id (UUID)
  - customerId (String)
  - priceOffered (BigDecimal)
  - featuresIncluded (List<String>)
  - converted (Boolean)
  - timestamp (Instant)

- **CustomerSegment**
  - id (UUID)
  - name (String)
  - criteria (List<String>)
  - size (Integer)
  - avgLtv (BigDecimal)
  - churnRate (Double)

- **ElasticityModel**
  - id (UUID)
  - segmentId (UUID)
  - priceRangeMin (BigDecimal)
  - priceRangeMax (BigDecimal)
  - elasticityCoefficient (Double)
  - rSquared (Double)

- **PriceRecommendation**
  - id (UUID)
  - segmentId (UUID)
  - currentPrice (BigDecimal)
  - recommendedPrice (BigDecimal)
  - confidenceScore (Double)
  - estimatedRevenueLift (Double)
  - rationale (String)

- **ChurnAnalysis**
  - id (UUID)
  - segmentId (UUID)
  - priceTier (String)
  - churnRate (Double)
  - priceSensitivity (Double)

## 2. API Endpoints

- `GET /api/segments`
  - Returns list of `CustomerSegment`
- `POST /api/elasticity/calculate`
  - Body: `{ "segmentId": "uuid" }`
  - Returns: Calculated `ElasticityModel`
- `GET /api/recommendations`
  - Returns list of `PriceRecommendation`
- `POST /api/recommendations/generate`
  - Body: `{ "segmentId": "uuid" }`
  - Returns: Generates recommendation and returns it.
- `GET /api/experiments`
  - Returns list of `PricingExperiment`
- `POST /api/experiments`
  - Body: `PricingExperiment` payload
  - Returns: Created `PricingExperiment`

## 3. Services

- **ElasticityModelingService**
  - Methods: `calculateElasticity(UUID segmentId)`
  - Behavior: Uses historical `ConversionRecord` data. Simulates calculation of coefficient.
  - Queue integration: Publishes `pricing-model.trained` via `QueueService`.

- **SegmentationService**
  - Methods: `getSegments()`

- **PriceOptimizationService**
  - Methods: `generateRecommendation(UUID segmentId)`
  - Behavior: Calculates optimum using `ElasticityModel`, then calls `AiService.chat()` to generate a human-readable rationale.

- **ExperimentDesignService**
  - Methods: `createExperiment(PricingExperiment request)`

- **WhatIfAnalysisService**
  - Methods: `simulateRevenue(UUID segmentId, BigDecimal newPrice)`

- **DiscountAnalysisService**
  - Methods: `analyzeDiscountEffectiveness()`

## 4. AI Prompt Example
"Based on the elasticity coefficient of {coefficient} for the {segmentName} segment, currently priced at {currentPrice}, write a brief rationale for changing the price to {recommendedPrice} aiming to maximize revenue while minimizing churn."

## 5. Frontend Pages
- `/app/dashboard/page.tsx`: Overview charts (Recharts) showing active experiments, top segments, and revenue lift.
- `/app/segments/page.tsx`: Table of customer segments and their elasticity curves (Plotly).
- `/app/experiments/page.tsx`: List and setup form for pricing experiments.
- `/app/recommendations/page.tsx`: AI-generated pricing recommendations.
