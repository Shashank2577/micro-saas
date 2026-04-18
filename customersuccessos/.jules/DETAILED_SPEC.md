# CustomerSuccessOS Detailed Specification

## Overview
CustomerSuccessOS is an AI customer success operations platform. It helps CSMs manage accounts by calculating real-time health scores, identifying expansion opportunities from usage patterns, and generating Quarterly Business Review (QBR) decks automatically using AI.

## Entities
1. **Account**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `name` (String)
   - `subscriptionTier` (String)
   - `csmName` (String)
   - `createdAt` (Timestamp)
   - `updatedAt` (Timestamp)
2. **HealthScore**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `accountId` (UUID)
   - `score` (Integer, 0-100)
   - `factors` (JSON - list of factors influencing score)
   - `recordedAt` (Timestamp)
3. **ExpansionOpportunity**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `accountId` (UUID)
   - `type` (String - e.g., 'UPSELL', 'CROSS_SELL')
   - `description` (String)
   - `estimatedValue` (Decimal)
   - `status` (String - e.g., 'IDENTIFIED', 'IN_PROGRESS', 'CLOSED')
   - `createdAt` (Timestamp)
4. **QbrDeck**
   - `id` (UUID)
   - `tenantId` (UUID)
   - `accountId` (UUID)
   - `content` (Text - AI generated QBR summary/deck content)
   - `status` (String - 'GENERATING', 'COMPLETED', 'FAILED')
   - `generatedAt` (Timestamp)

## REST Endpoints
* **Accounts**
  * `GET /api/accounts` - List accounts.
  * `POST /api/accounts` - Create account.
  * `GET /api/accounts/{id}` - Get account details.
* **Health Scores**
  * `GET /api/accounts/{id}/health` - Get latest health score.
  * `POST /api/accounts/{id}/health` - Record new health score.
* **Expansion Opportunities**
  * `GET /api/accounts/{id}/expansion` - List opportunities.
  * `POST /api/accounts/{id}/expansion` - Create opportunity.
* **QBR Generation**
  * `POST /api/accounts/{id}/qbr` - Trigger AI to generate QBR.
  * `GET /api/accounts/{id}/qbr` - Get generated QBRs.

## AI Integration
- When triggering a QBR generation (`POST /api/accounts/{id}/qbr`), the backend calls LiteLLM using `RestTemplate` with the context of the account, recent health scores, and expansion opportunities to generate a cohesive QBR markdown summary.

## Frontend (Next.js)
- **Dashboard (`/`)**: Overview of accounts, average health score, pending expansion value.
- **Accounts List (`/accounts`)**: Table of accounts with their current health score.
- **Account Details (`/accounts/[id]`)**:
  - Displays account info.
  - Health score chart/history.
  - List of expansion opportunities.
  - Section to view/generate QBR decks.

## Acceptance Criteria
- [ ] Backend can store and retrieve Accounts, HealthScores, ExpansionOpportunities, and QBRs with `tenantId` isolation.
- [ ] `POST /api/accounts/{id}/qbr` integrates with LiteLLM and saves the generated content.
- [ ] Frontend successfully displays the Dashboard, Account List, and Account Details pages.
- [ ] QBR generation can be triggered from the frontend, and the result is displayed.
