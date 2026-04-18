# RealEstateIntel Detailed Specification

## Core Concept
RealEstateIntel is an AI-powered real estate intelligence platform designed for real estate agents and investors. It provides automated comparable sales analysis, lease abstraction from PDF leases, investment return modeling, market trend monitoring, and portfolio health tracking.

## Phase 1: Database Schema

### Table: properties
- `id` (UUID, Primary Key)
- `tenant_id` (UUID, Foreign Key to tenants)
- `address` (VARCHAR)
- `city` (VARCHAR)
- `state` (VARCHAR)
- `zip_code` (VARCHAR)
- `property_type` (VARCHAR: SINGLE_FAMILY, MULTI_FAMILY, COMMERCIAL)
- `square_feet` (INTEGER)
- `bedrooms` (INTEGER)
- `bathrooms` (DECIMAL)
- `year_built` (INTEGER)
- `status` (VARCHAR: ACTIVE, PENDING, SOLD, OFF_MARKET)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: comparables
- `id` (UUID, Primary Key)
- `tenant_id` (UUID)
- `subject_property_id` (UUID, Foreign Key to properties)
- `comp_property_id` (UUID, Foreign Key to properties)
- `similarity_score` (DECIMAL)
- `price_adjusted` (DECIMAL)
- `notes` (TEXT)
- `created_at` (TIMESTAMP)

### Table: leases
- `id` (UUID, Primary Key)
- `tenant_id` (UUID)
- `property_id` (UUID, Foreign Key to properties)
- `tenant_name` (VARCHAR)
- `start_date` (DATE)
- `end_date` (DATE)
- `monthly_rent` (DECIMAL)
- `security_deposit` (DECIMAL)
- `document_url` (VARCHAR)
- `ai_summary` (TEXT)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Table: market_trends
- `id` (UUID, Primary Key)
- `tenant_id` (UUID)
- `zip_code` (VARCHAR)
- `month_year` (DATE)
- `median_sale_price` (DECIMAL)
- `days_on_market` (INTEGER)
- `inventory_count` (INTEGER)
- `created_at` (TIMESTAMP)

## Phase 2: Backend Services (Spring Boot)

### PropertyService
- `createProperty(PropertyDto)`
- `getPropertyById(UUID)`
- `listPropertiesByStatus(String status)`
- `updateProperty(UUID, PropertyDto)`

### ComparableService
- `generateComparables(UUID propertyId)`: Calls AI gateway to analyze and find comps based on property features.
- `getComparablesForProperty(UUID propertyId)`

### LeaseService
- `processLeaseDocument(MultipartFile file, UUID propertyId)`: Uploads document, calls AI to extract key terms (tenant, rent, dates), saves abstract.
- `getLeaseById(UUID)`

### MarketTrendService
- `getMarketTrends(String zipCode, int months)`: Fetches historical trend data.

## Phase 3: REST Endpoints

### Properties
- `POST /api/properties`: Create property
- `GET /api/properties/{id}`: Get property
- `GET /api/properties`: List properties
- `PUT /api/properties/{id}`: Update property

### Comparables
- `POST /api/properties/{id}/comparables/generate`: Trigger AI comp generation
- `GET /api/properties/{id}/comparables`: List comps

### Leases
- `POST /api/leases/upload`: Upload lease PDF for AI abstraction
- `GET /api/leases/{id}`: Get lease details
- `GET /api/properties/{id}/leases`: Get leases for a property

### Market Trends
- `GET /api/market-trends/{zipCode}`: Get trends for a zip code

## Phase 4: Frontend (Next.js)

### Pages
- `/dashboard`: High-level market trends and portfolio health
- `/properties`: List of all managed properties
- `/properties/[id]`: Property details, comparables, and leases
- `/properties/[id]/comparables`: Detailed comp analysis view
- `/leases/upload`: Interface to upload and abstract leases

### Components
- `PropertyCard`: Displays property summary
- `ComparableTable`: Sortable table of comps
- `LeaseAbstractView`: Displays AI-extracted lease terms
- `MarketTrendChart`: Line chart for median prices and DOM

## AI Integrations (LiteLLM)
- **Comparable Analysis Pipeline**: Given a subject property description, find similar properties and calculate similarity scores.
- **Lease Abstraction Pipeline**: Extract structured data (tenant name, rent, start/end dates, clauses) from raw PDF text.

## Acceptance Criteria
1. Property CRUD operations are fully functional and tenant-isolated.
2. AI Lease abstraction endpoint successfully mocks extracting data from text.
3. Comparables generation successfully mocks finding related properties.
4. Frontend dashboard loads and displays market trend charts.
