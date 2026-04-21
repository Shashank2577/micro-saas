# DETAILED SPEC

## GoalTracker Backend

This application is an AI-native financial goal-setting platform. It tracks users' goals and provides automated guidance.

### Architecture

- Spring Boot 3.x (Java 21)
- Maven
- PostgreSQL + Flyway
- JPA / Hibernate
- Integration with `cc-starter` framework

### API

**Goals CRUD**
- `POST /api/v1/goals` -> create a goal
- `GET /api/v1/goals` -> get list of goals
- `GET /api/v1/goals/{id}` -> get goal by id
- `PUT /api/v1/goals/{id}` -> update goal
- `DELETE /api/v1/goals/{id}` -> delete goal

### Data Model

- `goals`: Table for financial goals.
- `milestones`: Sub-goals/checkpoints.
- `contributions`: History of saved funds.
- `automated_transfers`: Scheduled tasks.
- `transfer_history`: Logs of execution.
- `goal_sharing`: For multi-user visibility.
- `motivation_history`: Nudges sent via AI.
