# CashflowAnalyzer

CashflowAnalyzer is a personal cash flow analysis and optimization platform. It provides deep visibility into income and spending patterns, identifies optimization opportunities, and recommends actions to improve financial health.

## Core Features
- Connect bank accounts via Plaid integration (mocked).
- Sync transactions and categorize them using AI/ML.
- Generate cash flow statements and analyze spending patterns.
- Budget comparison and trend analysis.
- Identify optimization opportunities and recommend savings.

## Architecture
- **Backend:** Spring Boot 3.3.5 (Java 21), PostgreSQL, LiteLLM (mocked).
- **Frontend:** Next.js (App Router), TypeScript, Tailwind CSS.
- Uses `cc-starter` for common configurations (tenant logic, security).

## Getting Started

### Prerequisites
- Java 21
- Node.js 18+
- Maven

### Running
Run backend with `mvn spring-boot:run`. Run frontend with `npm run build && npm start`.
