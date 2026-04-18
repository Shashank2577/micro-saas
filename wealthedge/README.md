# WealthEdge

WealthEdge is a high-net-worth wealth management micro-SaaS.

## Setup

Backend runs on port 8208. Frontend runs via Next.js.
Requires PostgreSQL, Redis, and LiteLLM gateway.

## Environment Variables

- `cc.ai.gateway-url`: The LiteLLM Gateway
- `cc.ai.api-key`: API key for LiteLLM

## Testing

Backend: `mvn clean test`
Frontend: `npm test`
