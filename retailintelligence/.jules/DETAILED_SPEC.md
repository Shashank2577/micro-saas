# RetailIntelligence Detailed Spec

## 1. Overview
**App Name:** RetailIntelligence
**Port:** 8080
**Description:** AI retail operations platform providing demand forecasting at the SKU level, dynamic pricing recommendations, and inventory optimization.
**Target Audience:** Retail and e-commerce operators.

## 2. Database Schema

```sql
CREATE TABLE sku (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    sku_code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    cost_price DECIMAL(10, 2),
    current_price DECIMAL(10, 2),
    stock_quantity INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE demand_forecast (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    sku_id UUID NOT NULL REFERENCES sku(id),
    forecast_date DATE NOT NULL,
    predicted_demand INT NOT NULL,
    confidence_score DECIMAL(5, 4),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE pricing_recommendation (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    sku_id UUID NOT NULL REFERENCES sku(id),
    recommended_price DECIMAL(10, 2) NOT NULL,
    reasoning TEXT,
    margin_percentage DECIMAL(5, 2),
    status VARCHAR(50) NOT NULL, -- PENDING, APPLIED, REJECTED
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## 3. Backend Implementation (Spring Boot)

### 3.1. Entities
- `Sku` (id, tenantId, skuCode, name, category, costPrice, currentPrice, stockQuantity)
- `DemandForecast` (id, tenantId, skuId, forecastDate, predictedDemand, confidenceScore)
- `PricingRecommendation` (id, tenantId, skuId, recommendedPrice, reasoning, marginPercentage, status)

### 3.2. REST Endpoints

#### Sku Controller
- `GET /api/skus` - List all SKUs (tenant-scoped)
- `POST /api/skus` - Create a new SKU
- `GET /api/skus/{id}` - Get SKU details

#### Demand Forecast Controller
- `GET /api/forecasts?skuId={skuId}` - Get forecasts for an SKU
- `POST /api/forecasts/generate` - Trigger AI forecast generation for an SKU

#### Pricing Recommendation Controller
- `GET /api/pricing-recommendations` - Get all pending recommendations
- `POST /api/pricing-recommendations/generate` - Trigger AI pricing recommendations
- `PUT /api/pricing-recommendations/{id}/apply` - Apply a recommendation to update SKU price

### 3.3. AI Integration (LiteLLM)
- **Forecast Generation**: `prompt: "Analyze historical data and trends for SKU {skuCode}. Predict demand for the next 7 days. Return JSON with { date, predictedDemand, confidenceScore }."`
- **Pricing Recommendation**: `prompt: "Analyze current stock, cost price {costPrice}, and current price {currentPrice} for SKU {skuCode}. Recommend a new price with reasoning, ensuring margin > 20%. Return JSON with { recommendedPrice, reasoning, marginPercentage }."`

## 4. Frontend Implementation (Next.js)

### 4.1. Pages
- `/` - Dashboard: Overview of total SKUs, pending pricing recommendations, and overall stock status.
- `/skus` - SKU Inventory: Table of SKUs with their stock levels, current price, and cost price.
- `/skus/[id]` - SKU Details: Shows demand forecast chart (react-chartjs-2) and pricing recommendations.
- `/pricing` - Pricing Actions: List of pending pricing recommendations to apply or reject.

### 4.2. Components
- `SkuTable`: Displays SKUs.
- `ForecastChart`: Visualizes predicted demand.
- `PricingActionList`: Handles applying/rejecting recommendations.

## 5. Integration Manifest
- **emits**: `sku.created`, `sku.price_updated`, `forecast.generated`
- **consumes**: `sales.order.placed` (to decrease stock)
