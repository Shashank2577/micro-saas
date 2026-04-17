# SocialIntelligence Detailed Specification

## 1. Overview
SocialIntelligence is a multi-platform social media analytics and growth intelligence application for creators. It aggregates metrics from Instagram, TikTok, YouTube, Twitter/X, and LinkedIn to provide unified analytics, audience insights, competitor benchmarking, and AI-powered growth recommendations.

## 2. Database Schema (PostgreSQL)

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE platform_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    platform_name VARCHAR(50) NOT NULL, -- INSTAGRAM, TIKTOK, YOUTUBE, TWITTER, LINKEDIN
    platform_account_id VARCHAR(255) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    access_token TEXT NOT NULL,
    refresh_token TEXT,
    token_expires_at TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(tenant_id, platform_name, platform_account_id)
);

CREATE TABLE engagement_metrics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    metric_date DATE NOT NULL,
    followers_count BIGINT DEFAULT 0,
    following_count BIGINT DEFAULT 0,
    likes_count BIGINT DEFAULT 0,
    comments_count BIGINT DEFAULT 0,
    shares_count BIGINT DEFAULT 0,
    views_count BIGINT DEFAULT 0,
    engagement_rate DECIMAL(5,2) DEFAULT 0.0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(account_id, metric_date)
);

CREATE TABLE audience_demographics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    demographic_type VARCHAR(50) NOT NULL, -- AGE, GENDER, LOCATION
    demographic_value VARCHAR(255) NOT NULL,
    percentage DECIMAL(5,2) NOT NULL,
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE content_analyses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    content_id VARCHAR(255) NOT NULL,
    content_url TEXT,
    content_type VARCHAR(50), -- POST, VIDEO, REEL, SHORT, TWEET
    published_at TIMESTAMP WITH TIME ZONE,
    likes BIGINT DEFAULT 0,
    comments BIGINT DEFAULT 0,
    shares BIGINT DEFAULT 0,
    views BIGINT DEFAULT 0,
    ai_pattern_analysis TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(account_id, content_id)
);

CREATE TABLE growth_recommendations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id VARCHAR(255) NOT NULL,
    platform_name VARCHAR(50) NOT NULL,
    recommendation_type VARCHAR(100) NOT NULL, -- CONTENT_STRATEGY, POSTING_TIME, ENGAGEMENT
    description TEXT NOT NULL,
    projected_impact TEXT,
    status VARCHAR(50) DEFAULT 'NEW', -- NEW, IMPLEMENTED, DISMISSED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX idx_platform_accounts_tenant ON platform_accounts(tenant_id);
CREATE INDEX idx_engagement_metrics_account_date ON engagement_metrics(account_id, metric_date);
CREATE INDEX idx_content_analyses_account ON content_analyses(account_id);
CREATE INDEX idx_growth_recommendations_tenant ON growth_recommendations(tenant_id);
```

## 3. REST API Endpoints

### 3.1. Accounts
- `GET /api/accounts`
  - Returns list of connected platform accounts for the tenant.
- `POST /api/accounts/connect/{platform}`
  - Initiates OAuth flow for the specific platform.
- `DELETE /api/accounts/{id}`
  - Disconnects an account and deletes associated metrics.

### 3.2. Metrics & Analytics
- `GET /api/metrics/unified`
  - Query Params: `startDate`, `endDate`
  - Returns aggregated metrics across all connected platforms.
- `GET /api/metrics/platform/{platform}`
  - Query Params: `startDate`, `endDate`
  - Returns metrics for a specific platform.
- `GET /api/metrics/engagement-trends`
  - Query Params: `period` (30, 90, 365)
  - Returns engagement rate trends over the specified period.

### 3.3. Audience
- `GET /api/audience/demographics`
  - Returns aggregated audience demographics.
- `GET /api/audience/demographics/{platform}`
  - Returns demographics for a specific platform.

### 3.4. Content
- `GET /api/content/top-performing`
  - Query Params: `limit` (default 10), `platform` (optional)
  - Returns top performing content based on engagement.
- `GET /api/content/{id}/analysis`
  - Returns detailed AI pattern analysis for a specific piece of content.

### 3.5. AI Growth Intelligence
- `GET /api/growth/recommendations`
  - Returns list of AI-generated growth recommendations.
- `POST /api/growth/recommendations/generate`
  - Triggers a new AI generation of recommendations based on recent metrics.
- `PUT /api/growth/recommendations/{id}/status`
  - Body: `{ "status": "IMPLEMENTED" }`
  - Updates the status of a recommendation.
- `GET /api/growth/projections`
  - Returns growth projections for the next 30 days.

## 4. Service Methods (Backend)

### 4.1. `OAuthManager`
- `String getAuthorizationUrl(String platform)`
- `PlatformAccount handleCallback(String platform, String code, String tenantId)`
- `void refreshTokens()`: Scheduled job to refresh expiring tokens.

### 4.2. `SocialMetricsAggregator`
- `void syncMetrics(String tenantId)`: Fetches latest data from platform APIs.
- `UnifiedMetrics getUnifiedMetrics(String tenantId, LocalDate start, LocalDate end)`
- `EngagementTrends getEngagementTrends(String tenantId, int days)`

### 4.3. `AnalyticsService`
- `AudienceDemographics getAggregatedDemographics(String tenantId)`
- `List<ContentAnalysis> getTopPerformingContent(String tenantId, int limit)`
- `void analyzeContentPatterns(String tenantId)`

### 4.4. `AIRecommendationService`
- `List<GrowthRecommendation> generateRecommendations(String tenantId)`: Calls LiteLLM.
- `String getGrowthProjections(String tenantId)`: Calls LiteLLM with historical data.
- `String analyzeContent(ContentAnalysis content)`: Calls LiteLLM for pattern analysis.

## 5. React Components (Frontend)

### 5.1. Pages
- `/dashboard`: Main unified dashboard.
- `/platforms`: Platform connection management.
- `/audience`: Detailed audience insights.
- `/content`: Top performing content and pattern analysis.
- `/growth`: AI recommendations and projections.

### 5.2. Components
- `MetricCard`: Displays a single metric with trend indicator.
- `UnifiedMetricsChart`: Line chart for engagement trends (Chart.js/Recharts).
- `PlatformConnectionCard`: Manages OAuth connection for a specific platform.
- `DemographicsPieChart`: Pie chart for audience distribution.
- `ContentPerformanceTable`: List of top content with thumbnail and metrics.
- `RecommendationCard`: Displays a single AI growth recommendation with actions.

## 6. AI Integration Points

### 6.1. LiteLLM Setup
- Endpoint: `http://localhost:4000/v1/chat/completions` (or similar configured local instance).
- Model: `gpt-4` or equivalent mapped model.

### 6.2. Prompts
- **Growth Recommendations:** "Based on the following engagement metrics across [Platforms] over the last 30 days: [Data], generate 5 specific, actionable growth recommendations."
- **Content Pattern Analysis:** "Analyze the following top-performing posts: [Post Data]. Identify common patterns (e.g., timing, format, topics) that contribute to their success."
- **Growth Projections:** "Given the historical follower growth: [Data], project the expected growth for the next 30 days and provide key drivers."

## 7. Event Integration (integration-manifest.json)
- Emits: `metrics.synced`, `recommendations.generated`
- Consumes: `user.created` (for tenant provisioning)
