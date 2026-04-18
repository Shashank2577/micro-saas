# MarketplaceHub Detailed Specification

## 1. Overview
MarketplaceHub is the central app discovery and marketplace system. It catalogs all microapps, enables app discovery, handles app ratings/reviews, and manages app installation for workspaces.

## 2. Database Schema

```sql
CREATE TABLE apps (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255),
    tags JSONB,
    screenshots JSONB,
    pricing_tiers JSONB,
    developer_id VARCHAR(255),
    version VARCHAR(50),
    status VARCHAR(50) DEFAULT 'PUBLIC',
    trending_score INT DEFAULT 0,
    total_installations INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE app_installations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    app_id UUID NOT NULL,
    workspace_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'TRIAL',
    trial_ends_at TIMESTAMP,
    installed_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE app_reviews (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    app_id UUID NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    rating DECIMAL(3,2) NOT NULL,
    review_text TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE app_revenues (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    app_id UUID NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE permission_grants (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    installation_id UUID NOT NULL,
    permission_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'GRANTED',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## 3. REST API Endpoints

### 3.1. App Catalog
- `GET /api/v1/apps` - List apps with filtering (category, search text) and pagination.
- `GET /api/v1/apps/{id}` - Get app details.
- `POST /api/v1/apps` - Register a new app (developer).

### 3.2. App Installation
- `POST /api/v1/apps/{id}/install` - Initiate app installation. Request body includes `workspaceId` and requested `permissions`.
- `GET /api/v1/installations/workspace/{workspaceId}` - List installed apps for a workspace.

### 3.3. Reviews
- `POST /api/v1/apps/{id}/reviews` - Submit a review for an app.
- `PUT /api/v1/reviews/{reviewId}/status` - Update review status (admin moderation).
- `GET /api/v1/apps/{id}/reviews` - List approved reviews for an app.

### 3.4. Analytics & Trending
- `GET /api/v1/apps/trending` - Get trending apps based on installation count / trending score.
- `GET /api/v1/apps/{id}/revenue` - Get app revenue stats.

### 3.5. AI Search
- `POST /api/v1/apps/search` - AI-powered full-text search and recommendation (uses LiteLLM).

## 4. Frontend Components

### 4.1. Pages
- `app/page.tsx` - App Catalog / Home. Shows trending apps, search bar, and categories.
- `app/apps/[id]/page.tsx` - App Details page. Shows screenshots, description, pricing, and reviews.
- `app/apps/[id]/install/page.tsx` - Installation workflow with permission grants and trial start.
- `app/workspace/page.tsx` - Workspace dashboard showing installed apps.
- `app/admin/reviews/page.tsx` - Admin moderation for pending reviews.

### 4.2. Components
- `AppCard` - Reusable card showing app name, category, rating, and icon.
- `CategoryFilter` - Sidebar or pill list to filter apps.
- `ReviewSection` - Component to list reviews and submit a new one.
- `InstallModal` - Modal to confirm permissions during installation.
- `AppAnalytics` - Developer/admin view of total installations and revenue.

## 5. Acceptance Criteria

1. WealthEdge app can be registered with 5 screenshots and pricing.
2. WealthEdge can be categorized under Finance and Wealth Management.
3. Search for 'wealth' returns WealthEdge.
4. User clicks Install on WealthEdge, prompted for 'read:user_profile' permission.
5. User approves, workspace gets WealthEdge, shown in switcher.
6. User rates WealthEdge 4.5 stars.
7. Admin approves review, it appears on the app page.
8. WealthEdge shows 500 installations in analytics.
9. Version v2.0 released, update logic triggered.
10. Revenue of $5,000 tracked for WealthEdge.
11. 14-day free trial enabled upon install.
