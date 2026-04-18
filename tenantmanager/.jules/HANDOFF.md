# Handoff Notes

## Questions Resolved
- Q: What exactly defines a "tenant" in this app?
- A: Since this is an app used by SaaS builders, the "system tenant" (the `tenant_id` column) represents the SaaS builder. The `CustomerTenant` entity represents *their* customer. So it's managing tenants within a tenant.

## Assumptions
- LiteLLM is used for the AI analysis of tenant health. We will mock the events in the test and provide a few-shot prompt for the AI.
- Event ingestion from other apps isn't fully implemented in a live system yet (no active Kafka/RabbitMQ in dev), so we will provide an endpoint to create events manually or via REST.

## Future Work
- Implement actual event listeners (Kafka/RabbitMQ) for real-time ingestion of usage data from other ecosystem apps.
- Implement automated workflows based on churn risk.
