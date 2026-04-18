# RestaurantIntel - Detailed Specification

## Overview
RestaurantIntel is an AI-powered restaurant operations platform designed to help food & beverage operators make data-driven decisions. It tackles core industry challenges: food waste (which costs 4–10% of revenue), manual staff scheduling, and intuitive menu design. 

## Key Features
1. **Menu Performance Analysis:** Evaluates menu items based on profit margin and popularity (Menu Engineering Matrix: Stars, Plowhorses, Puzzles, Dogs).
2. **Predictive Ordering:** Uses historical data and AI predictive pipelines to recommend ingredient orders, reducing food waste.
3. **Staff Scheduling Optimization:** Recommends demand-based staffing levels using predictive models.
4. **Review Sentiment Analysis:** Aggregates multi-platform reviews and uses AI to extract operational insights.

## Architecture

### Backend (Spring Boot 3.3.5 / Java 21)
- **Port:** 8080
- **Database:** PostgreSQL (tenant-isolated queries)
- **AI Integration:** LiteLLMClient for predictive pipelines and review sentiment analysis.
- **Cross-Cutting Concerns:** Multi-tenancy, RBAC, exception handling via `cc-starter`.

### Frontend (Next.js App Router / React / TypeScript / Tailwind)
- **Framework:** Next.js with App Router
- **State Management:** React Hooks
- **Styling:** Tailwind CSS + shadcn/ui (or similar)
- **Charting:** react-chartjs-2 for menu matrix and predictive trends.

## Database Schema (Entities)

All tables must include: `id` (UUID), `tenant_id` (UUID, indexed), `created_at` (Timestamp), `updated_at` (Timestamp).

1. **MenuItem**
   - `name` (String)
   - `category` (String)
   - `cost` (BigDecimal) - Cost of goods sold (COGS)
   - `price` (BigDecimal) - Selling price
   - `units_sold` (Integer) - Over a specific period
   - `profit_margin` (BigDecimal) - Computed (price - cost)

2. **Ingredient**
   - `name` (String)
   - `unit` (String) - e.g., kg, liters, units
   - `current_stock` (BigDecimal)
   - `par_level` (BigDecimal) - Minimum stock before reordering

3. **PredictiveOrder**
   - `ingredient_id` (UUID, FK to Ingredient)
   - `predicted_demand` (BigDecimal)
   - `recommended_order_amount` (BigDecimal)
   - `order_date` (LocalDate)
   - `status` (String) - PENDING, APPROVED, REJECTED
   - `ai_confidence_score` (BigDecimal)
   - `ai_rationale` (Text)

4. **StaffSchedule**
   - `role` (String)
   - `date` (LocalDate)
   - `shift_start` (LocalTime)
   - `shift_end` (LocalTime)
   - `predicted_busyness` (String) - LOW, MEDIUM, HIGH, VERY_HIGH
   - `recommended_staff_count` (Integer)

5. **CustomerReview**
   - `source` (String) - Yelp, Google, etc.
   - `rating` (Integer) - 1 to 5
   - `content` (Text)
   - `review_date` (LocalDate)
   - `sentiment` (String) - POSITIVE, NEUTRAL, NEGATIVE
   - `operational_insight` (Text) - Extracted by AI (e.g., "Food is cold", "Service slow")

## REST Endpoints

### 1. Menu Intelligence
- `GET /api/v1/menu-items` - List all menu items with engineering matrix classifications.
- `POST /api/v1/menu-items` - Create a new menu item.
- `POST /api/v1/menu-items/analyze` - Trigger AI analysis on the menu to provide recommendations (e.g., "Raise price on Plowhorse item X").

### 2. Predictive Ordering
- `GET /api/v1/ingredients` - List inventory.
- `GET /api/v1/orders/predictive` - Get AI-recommended orders for the upcoming week.
- `POST /api/v1/orders/predictive/generate` - Trigger AI to generate order predictions based on current stock and historical usage trends.
- `PUT /api/v1/orders/predictive/{id}/status` - Approve or reject an order recommendation.

### 3. Staff Scheduling
- `GET /api/v1/schedules` - Get schedule recommendations.
- `POST /api/v1/schedules/generate` - Trigger AI to predict staffing needs based on expected demand.

### 4. Review Sentiment
- `GET /api/v1/reviews` - List reviews with AI sentiment.
- `POST /api/v1/reviews` - Add a review.
- `POST /api/v1/reviews/{id}/analyze` - Force AI analysis on a specific review.

## Frontend Pages

1. **Dashboard (`/`)**
   - High-level KPIs: Estimated food waste reduction, average review sentiment, upcoming shift demand.
2. **Menu Analysis (`/menu`)**
   - Table of menu items.
   - 2x2 scatter plot chart for the Menu Engineering Matrix (Profitability vs. Popularity).
3. **Predictive Ordering (`/ordering`)**
   - List of AI recommended orders.
   - Approve/Reject buttons.
   - Rationale display (Why AI suggested this).
4. **Staff Scheduling (`/scheduling`)**
   - Daily/weekly view of required staffing vs predicted busyness.
5. **Review Intelligence (`/reviews`)**
   - List of reviews.
   - Operational insights dashboard (e.g., highlighting common complaints).

## AI Integration Points

- **LiteLLMClient (`/api/chat/completions`)**
  - **Menu Strategy Agent:** Prompts the model with the menu data to generate strategic advice.
  - **Ordering Prediction Agent:** Simplistic predictive model (using LLM as a proxy for ML in this scope) to suggest order amounts.
  - **Review Extraction Agent:** Extracts sentiment and operational issues from raw review text.

## Acceptance Criteria
1. Backend enforces `tenant_id` for all queries.
2. Menu API successfully calculates or allows client to calculate Stars, Plowhorses, Puzzles, and Dogs.
3. Predictive ordering endpoints return structured order objects with AI rationale.
4. Staff scheduling handles date/time logic and maps to demand.
5. Review AI successfully parses a mock review and saves sentiment.
6. Frontend displays navigation and all required views.
7. Integration manifest contains relevant capabilities and emissions.
8. Component and Unit tests pass. No TODOs.

## Error Handling
- Use Spring `@RestControllerAdvice` (from `cc-starter`) for consistent error formatting.
- Handle AI timeouts gracefully (fallback to default logic or return clear error message).
