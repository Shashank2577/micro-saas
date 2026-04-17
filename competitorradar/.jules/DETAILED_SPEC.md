# CompetitorRadar Detailed Specification

## Core Entities
1. **Competitor**: Defines a tracked competitor. (id, tenant_id, name, website, industry, description, created_at, updated_at).
2. **ProductChange**: Record of a change detected on a competitor's website/product. (id, tenant_id, competitor_id, title, description, url, detected_at, status).
3. **PricingChange**: Historical pricing change for a competitor. (id, tenant_id, competitor_id, old_price, new_price, plan_name, detected_at).
4. **HiringSignal**: Job postings or hiring trends. (id, tenant_id, competitor_id, role_title, department, location, source, posted_at).
5. **CustomerReview**: Sentiment from review platforms. (id, tenant_id, competitor_id, platform, rating, text, category, sentiment_score, posted_at).
6. **SocialMention**: Social media activity. (id, tenant_id, competitor_id, platform, text, url, sentiment_score, posted_at).
7. **PressMention**: News/press articles. (id, tenant_id, competitor_id, source, title, url, sentiment_score, published_at).
8. **Battlecard**: AI-generated battlecard for a competitor. (id, tenant_id, competitor_id, content, generated_at).
9. **WinLossRecord**: Tracks win/loss vs a competitor. (id, tenant_id, competitor_id, outcome (WIN/LOSS), reason, value, date).
10. **Feature**: Features tracked in comparison matrix. (id, tenant_id, name, category).
11. **CompetitorFeature**: Matrix mapping. (id, tenant_id, competitor_id, feature_id, status (SUPPORTED/PARTIAL/NO)).
12. **Alert**: Notification of material change. (id, tenant_id, type, severity, message, competitor_id, created_at).

## Backend Endpoints
- `GET /api/competitors`: List competitors.
- `POST /api/competitors`: Add competitor.
- `DELETE /api/competitors/{id}`: Remove competitor.
- `GET /api/competitors/{id}/battlecard`: Get latest battlecard.
- `POST /api/competitors/{id}/battlecard/generate`: Trigger AI battlecard generation.
- `GET /api/competitors/{id}/product-changes`: List product changes.
- `GET /api/competitors/{id}/pricing-changes`: List pricing changes.
- `GET /api/competitors/{id}/hiring-signals`: List hiring signals.
- `GET /api/competitors/{id}/reviews`: List reviews & sentiment.
- `GET /api/competitors/{id}/social`: List social mentions.
- `GET /api/competitors/{id}/press`: List press mentions.
- `GET /api/alerts`: List alerts.
- `GET /api/win-loss`: Dashboard stats for win/loss.
- `POST /api/win-loss`: Record win/loss.
- `GET /api/features/matrix`: Get feature comparison matrix.
- `GET /api/reports/quarterly`: Get quarterly report data.

## Frontend
Pages:
- `/dashboard`: High-level alerts, recent changes, quick win/loss view.
- `/competitors`: Watchlist management.
- `/competitors/[id]`: Detail view for a competitor (battlecard, changes, hiring, reviews).
- `/win-loss`: Win/loss analysis dashboard.
- `/matrix`: Feature comparison matrix.
- `/reports`: Competitive landscape report generation/view.

Components:
- `CompetitorList`, `BattlecardView`, `ChangeTimeline`, `SentimentChart`, `PricingHistoryChart`, `AlertFeed`, `WinLossChart`, `FeatureMatrixTable`.

## AI Integration
- `BattlecardGeneratorService`: Uses LiteLLM to synthesize recent changes, pricing, and reviews into a comprehensive battlecard.
- `ReportGeneratorService`: Uses LiteLLM to synthesize quarterly landscape report.

## Integration Manifest
- Emits: `competitor.alert.generated`, `competitor.battlecard.updated`
- Consumes: `crm.winloss.recorded` (simulated internally for now)

## Acceptance Tests
- Add/remove competitor verification.
- Battlecard generation triggers LLM.
- Feature matrix updates.
- Win/loss aggregation returns correct stats.
- Proper multi-tenant filtering on all endpoints.
