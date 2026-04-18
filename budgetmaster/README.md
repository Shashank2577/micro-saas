# BudgetMaster

BudgetMaster is a personal budgeting and expense control platform. Provides zero-based budgeting, spending controls, category management, and real-time alerts to help users manage money more effectively.

## Features
- Zero-based budgeting framework
- Category-based budget allocation (Envelope system)
- Real-time budget vs actual tracking
- AI-driven optimization recommendations
- Savings targets and review checklists

## Setup
To run the backend locally:
\`\`\`bash
cd backend
mvn spring-boot:run
\`\`\`

To run the frontend locally:
\`\`\`bash
cd frontend
npm install
npm run build
npm run start &
\`\`\`

## Configuration
Backend runs on port 8209.
Database uses PostgreSQL.

## Testing
Backend tests: \`mvn clean verify\`
