# SupportIntelligence

SupportIntelligence is an AI support co-pilot that provides real-time response suggestions for support agents. It analyzes past tickets, knowledge base articles, and product docs to provide high-quality responses.

## Key Features
*   **Real-time AI Suggestions**: Grounded in your knowledge base and past tickets.
*   **Escalation Detection**: Identifies customer anger, complexity, or risk.
*   **Insights & Patterns**: Mines ticket patterns to aggregate product issues.
*   **Agent Metrics**: Tracks agent efficiency and CSAT scores.

## Architecture
Built on the shared cc-starter foundation stack.
*   **Backend**: Spring Boot 3.3.5 (Java 21), PostgreSQL with pgvector, LiteLLM.
*   **Frontend**: Next.js 14, Tailwind CSS.

## Getting Started

### Local Development

1.  **Backend**:
    cd backend
    mvn clean install
    mvn spring-boot:run

2.  **Frontend**:
    cd frontend
    npm install
    npm run build

### Testing
*   Backend: mvn test
*   Frontend: npm run test
