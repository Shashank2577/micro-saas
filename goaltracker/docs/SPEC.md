# GoalTracker Specification

## 1. Overview
GoalTracker is a financial goal-setting and achievement platform with AI-powered recommendations. It enables users to define savings goals, track progress, automate contributions, and receive personalized guidance to achieve financial milestones.

## 2. Core Features
- Goal creation with target amount, deadline, category, and priority.
- Automated savings plan generation.
- Progress tracking with milestone visualization (e.g., 25%, 50%, 75%, 100%).
- Multi-account savings destination mapping.
- Automated contribution scheduling and transfers.
- AI-driven investment recommendations based on time horizons.
- Motivation insights and behavioral nudges via AI.
- Catch-up acceleration analysis (windfalls).
- Goal pausing/modification.
- Goal sharing with accountability partners.

## 3. Database Schema
- **goals**: id, tenant_id, user_id, title, category, target_amount, current_amount, deadline, priority, status, created_at, updated_at
- **milestones**: id, goal_id, percentage, amount, achieved, achieved_at
- **contributions**: id, goal_id, amount, source_account_id, destination_account_id, date, status, type (manual, automated, windfall)
- **automated_transfers**: id, goal_id, amount, frequency, source_account_id, destination_account_id, next_run_date, status
- **transfer_history**: id, transfer_id, amount, date, status
- **goal_sharing**: id, goal_id, shared_with_email, permissions
- **motivation_history**: id, goal_id, nudge_message, sent_at

## 4. API Endpoints
- `POST /api/v1/goals`
- `GET /api/v1/goals`
- `GET /api/v1/goals/{id}`
- `PUT /api/v1/goals/{id}`
- `DELETE /api/v1/goals/{id}`
- `POST /api/v1/goals/{id}/pause`
- `GET /api/v1/goals/{id}/progress`
- `GET /api/v1/goals/{id}/milestones`
- `POST /api/v1/goals/{id}/contributions`
- `POST /api/v1/goals/{id}/windfalls`
- `POST /api/v1/goals/{id}/transfers`
- `POST /api/v1/goals/{id}/transfers/pause`
- `GET /api/v1/goals/{id}/savings-plan`
- `GET /api/v1/goals/{id}/investments`
- `GET /api/v1/goals/{id}/acceleration`
- `GET /api/v1/goals/{id}/nudge`
- `POST /api/v1/goals/{id}/share`

## 5. Frontend Pages
- **Dashboard**: Overview of all active goals and progress.
- **Goal Creation**: Form to create a new goal.
- **Goal Details**: Detailed view of a goal, including milestones, contributions, savings plan, and AI recommendations.
- **Transfers**: Management of automated transfers.
- **History**: Past completed goals.

## 6. Architecture
- **Entities**: JPA entities mapped to PostgreSQL tables.
- **Repositories**: Spring Data JPA repositories.
- **Services**: Business logic, including AI integrations (LiteLLM) and scheduling.
- **Controllers**: REST APIs.
- **Frontend**: Next.js App Router, Tailwind, Recharts.
- **Events**: Integration manifest defines events emitted by GoalTracker.
