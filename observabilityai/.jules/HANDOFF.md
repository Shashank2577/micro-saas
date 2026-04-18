# Handoff Notes

## Questions Resolved During Build
- Q: There was no `omc/sessions/observabilityai.json`. How to establish the schema?
  A: Extracted context from `docs/superpowers/specs/2026-04-15-micro-saas-ecosystem-design.md`, section J6.

## Assumptions
- LiteLLM gateway: We've stubbed out the AI connection to use a mock request, as the AI gateway URL (`http://localhost:4000/v1/chat/completions`) may not be fully available during testing. It returns a mock JSON response natively if an error occurs to prevent failures in demo flow.
- No elaborate charts (e.g., Chart.js) implemented to maintain simplicity, relying on standard Next.js table representations and lists for the dashboard.

## Future Work
- [ ] Connect the LiteLLM client seamlessly with genuine OpenTelemetry signals via OTLP endpoints instead of direct REST API payloads.
- [ ] Implement an Alert rule engine using Webhooks or Kafka to trigger automatic anomaly detections rather than manual endpoints.
- [ ] Build interactive Chart.js modules inside Next.js components to visualize signal volume anomalies.
- [ ] Complete E2E testing using Playwright with properly hydrated database signals.
