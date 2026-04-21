import os
import re

file_path = 'socialintelligence/backend/src/main/resources/db/migration/V1__init.sql'

with open(file_path, 'r') as f:
    content = f.read()

# Update PlatformAccount
content = re.sub(r'CREATE TABLE platform_accounts \([\s\S]*?\);', '''CREATE TABLE platform_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    platform VARCHAR(50) CHECK (platform IN ('INSTAGRAM','TIKTOK','YOUTUBE','TWITTER','LINKEDIN')) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    account_id_external VARCHAR(255) NOT NULL,
    access_token_encrypted TEXT NOT NULL,
    refresh_token_encrypted TEXT,
    token_expires_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    connected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, platform, account_id_external)
);''', content)

# Update EngagementMetric
content = re.sub(r'CREATE TABLE engagement_metrics \([\s\S]*?\);', '''CREATE TABLE engagement_metrics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    metric_date DATE NOT NULL,
    followers_count BIGINT DEFAULT 0,
    impressions BIGINT DEFAULT 0,
    reach BIGINT DEFAULT 0,
    likes BIGINT DEFAULT 0,
    comments BIGINT DEFAULT 0,
    shares BIGINT DEFAULT 0,
    engagement_rate DECIMAL(6,4) DEFAULT 0.0000,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(account_id, metric_date)
);''', content)

# Replace ContentAnalyses with ContentPost
content = re.sub(r'CREATE TABLE content_analyses \([\s\S]*?\);', '''CREATE TABLE content_posts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    external_post_id VARCHAR(255) NOT NULL,
    content_type VARCHAR(50),
    caption TEXT,
    posted_at TIMESTAMP,
    likes BIGINT DEFAULT 0,
    comments BIGINT DEFAULT 0,
    shares BIGINT DEFAULT 0,
    views BIGINT DEFAULT 0,
    engagement_rate DECIMAL(6,4) DEFAULT 0.0000,
    UNIQUE(account_id, external_post_id)
);''', content)

# Update AudienceDemographic
content = re.sub(r'CREATE TABLE audience_demographics \([\s\S]*?\);', '''CREATE TABLE audience_demographics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    account_id UUID REFERENCES platform_accounts(id) ON DELETE CASCADE,
    age_range VARCHAR(50),
    gender VARCHAR(50),
    country VARCHAR(100),
    percentage DECIMAL(5,2) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);''', content)

# Update GrowthRecommendation
content = re.sub(r'CREATE TABLE growth_recommendations \([\s\S]*?\);', '''CREATE TABLE growth_recommendations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    recommendation_text TEXT NOT NULL,
    platform VARCHAR(50),
    category VARCHAR(100),
    priority INT,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_actioned BOOLEAN DEFAULT FALSE
);''', content)

content = re.sub(r'CREATE INDEX idx_content_analyses_account ON content_analyses\(account_id\);', 'CREATE INDEX idx_content_posts_account ON content_posts(account_id);', content)

with open(file_path, 'w') as f:
    f.write(content)
