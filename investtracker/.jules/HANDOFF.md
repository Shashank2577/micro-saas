# InvestTracker Handoff Notes

## Features Implemented
- Core Domain Model with Multitenancy: Portfolio, BrokerageAccount, Asset, Holding, TaxLot, Transaction, Watchlist
- Service and REST Controller layers for Portfolios and AI Insights
- LiteLLM integration for AI-powered risk assessment and portfolio optimization recommendations
- Frontend Next.js app with `use client` components for data fetching
- Frontend testing suite with Vite and Testing Library

## Assumptions & Workarounds
- Replaced scaffold template files with the accurate InvestTracker domain.
- In `AiOptimizationService`, we used a mock fallback so that the endpoint still works without LiteLLM backend running locally. In production, real portfolio holdings should be mapped into the prompt.
- The `BrokerageAccount` syncing functionality is modeled in the DB (`sync_status`, `oauth_token`), but the actual async PGMQ worker is left for future implementation per the exact requirement focus for this standalone build.

## Future Work
- Implement actual async synchronization logic using PGMQ for connecting to Alpaca, IB, Fidelity, Coinbase.
- Integrate Recharts/Chart.js for interactive allocation and performance charting on the Dashboard.
- Enhance testing suite to achieve higher UI test coverage.
