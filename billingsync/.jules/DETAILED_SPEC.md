# BillingSync - Detailed Specification

## 1. Overview
BillingSync is a metered billing and subscription management system. It tracks usage, calculates charges, manages subscriptions, invoicing, and integrates with payment processors for all apps.

## 2. Database Schema (PostgreSQL)

### Table: `subscription_plans`
- `id` (UUID, PK)
- `tenant_id` (VARCHAR)
- `name` (VARCHAR)
- `description` (TEXT)
- `base_price` (DECIMAL)
- `billing_period` (VARCHAR) -- MONTHLY, ANNUALLY
- `currency` (VARCHAR)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: `pricing_models`
- `id` (UUID, PK)
- `tenant_id` (VARCHAR)
- `plan_id` (UUID, FK)
- `metric_name` (VARCHAR) -- e.g., 'api_calls'
- `model_type` (VARCHAR) -- FLAT_RATE, PER_UNIT, TIERED, VOLUME
- `unit_price` (DECIMAL)
- `included_units` (INTEGER)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: `subscriptions`
- `id` (UUID, PK)
- `tenant_id` (VARCHAR)
- `plan_id` (UUID, FK)
- `status` (VARCHAR) -- ACTIVE, CANCELED, PAST_DUE
- `current_period_start` (TIMESTAMP)
- `current_period_end` (TIMESTAMP)
- `payment_method_id` (VARCHAR) -- Stripe payment method ID
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: `meter_events`
- `id` (UUID, PK)
- `tenant_id` (VARCHAR)
- `subscription_id` (UUID, FK)
- `metric_name` (VARCHAR)
- `quantity` (INTEGER)
- `timestamp` (TIMESTAMP)
- `created_at` (TIMESTAMP)

### Table: `invoices`
- `id` (UUID, PK)
- `tenant_id` (VARCHAR)
- `subscription_id` (UUID, FK)
- `amount_due` (DECIMAL)
- `amount_paid` (DECIMAL)
- `tax_amount` (DECIMAL)
- `status` (VARCHAR) -- DRAFT, OPEN, PAID, VOID, UNCOLLECTIBLE
- `due_date` (TIMESTAMP)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: `invoice_line_items`
- `id` (UUID, PK)
- `tenant_id` (VARCHAR)
- `invoice_id` (UUID, FK)
- `description` (VARCHAR)
- `amount` (DECIMAL)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: `payments`
- `id` (UUID, PK)
- `tenant_id` (VARCHAR)
- `invoice_id` (UUID, FK)
- `amount` (DECIMAL)
- `status` (VARCHAR) -- PENDING, SUCCEEDED, FAILED
- `processor_transaction_id` (VARCHAR)
- `error_message` (TEXT)
- `retry_count` (INTEGER)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: `refunds`
- `id` (UUID, PK)
- `tenant_id` (VARCHAR)
- `payment_id` (UUID, FK)
- `amount` (DECIMAL)
- `reason` (VARCHAR)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## 3. REST API Endpoints

### Plans & Pricing
- `GET /api/plans` - List plans
- `POST /api/plans` - Create a plan
- `GET /api/plans/{id}` - Get plan details
- `POST /api/plans/{id}/pricing-models` - Add pricing model to plan

### Subscriptions
- `GET /api/subscriptions` - List subscriptions
- `POST /api/subscriptions` - Create a subscription
- `GET /api/subscriptions/{id}` - Get subscription details
- `POST /api/subscriptions/{id}/change-plan` - Change subscription plan (calculates prorated charges)
- `POST /api/subscriptions/{id}/cancel` - Cancel subscription

### Metering
- `POST /api/meter-events` - Record usage events

### Invoices & Billing
- `GET /api/invoices` - List invoices
- `GET /api/invoices/{id}` - Get invoice details
- `POST /api/invoices/{id}/pay` - Attempt to pay invoice
- `POST /api/invoices/{id}/refund` - Refund an invoice payment
- `GET /api/billing/report` - Get billing revenue report
- `GET /api/billing/recommendations` - Get LiteLLM revenue optimization recommendations

## 4. Frontend Components (Next.js)

### Pages
- `/` - Dashboard with revenue metrics, recent invoices, active subscriptions
- `/subscriptions` - List of subscriptions and their status
- `/subscriptions/new` - Create a new subscription
- `/plans` - List of plans and pricing models
- `/invoices` - List of invoices
- `/reports` - Revenue reporting page with AI recommendations

### Components
- `MetricCard` - Display key metrics (MRR, Active Subs)
- `InvoiceList` - Table displaying recent invoices
- `SubscriptionList` - Table displaying subscriptions
- `CreatePlanModal` - Form to create new plan
- `MeterEventForm` - Form to submit usage event

## 5. Integrations & Third-Party
- **Stripe**: Will be mocked for tests and implementation logic but integrated conceptually via a `PaymentProcessorService` interface.
- **LiteLLM**: Used for generating insights on revenue or pricing based on `GET /api/billing/recommendations`.
- **Redis**: Used to cache aggregated meter events before saving to DB to avoid DB write-heavy operations on every single API call (not fully enforced in memory due to scope, but service will be prepared).

## 6. Error Handling
- Invalid Plan Change (e.g., plan does not exist) -> 400 Bad Request
- Over quota -> Usage records can be flagged or rate-limited via an alert system -> 429 Too Many Requests or warning.
- Payment Failure -> Sets status to FAILED, increments retry_count.

## 7. Acceptance Criteria Check
1. Meter event published -> `POST /api/meter-events` with 150 API calls
2. Plan defined -> `POST /api/plans` and `POST /api/plans/{id}/pricing-models` (per-unit, 10k included, $0.05)
3. Invoice generated -> `POST /api/invoices/generate` (internal scheduler or manual trigger) calculates 150 calls
4. Subscription changed -> `POST /api/subscriptions/{id}/change-plan` handles proration
5. Payment processed -> Payment Service handles charge successfully
6. Payment failed -> Payment Service handles retry logic
7. Tax calculated -> Tax included in Invoice generation (e.g. 8%)
8. Usage quota enforced -> Meter service checks if `quantity > included_units`
9. Refund issued -> `POST /api/invoices/{id}/refund` generates credit note/refund record
10. Billing report -> `GET /api/billing/report` shows total MRR/revenue
