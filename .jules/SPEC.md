# SocialIntelligence Base Specification

## Requirements
- OAuth integration: Instagram, TikTok, YouTube, Twitter/X, LinkedIn
- Unified analytics dashboard (cross-platform metrics)
- Engagement rate trends (30/90/365 day views)
- Audience demographics aggregation
- Top-performing content analysis
- AI growth intelligence engine
- Content calendar with performance prediction
- Audience insights (overlap, sentiment, influencers)
- Competitor benchmarking
- Growth trajectory projections

## Constraints
- OAuth refresh tokens securely stored
- API rate limits respected (batch requests, cache)
- Metrics synced max every 15 minutes
- Historical data retained for 2 years
- No hardcoded API keys
- GDPR compliant (encryption at rest)

## Acceptance Criteria
- User can connect Instagram, TikTok, YouTube, Twitter/X, LinkedIn
- Dashboard displays unified metrics across platforms
- AI provides 5+ growth recommendations
- Engagement trends visualized (30/90/365 days)
- Content calendar with platform-specific scheduling
- Audience demographics shown (aggregate + per-platform)
- Top-performing content flagged with pattern analysis
- Growth projection for next 30 days
- Integration manifest defines metrics API
- Multi-tenant isolation enforced
