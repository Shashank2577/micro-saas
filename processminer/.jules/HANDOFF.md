# Handoff Notes

## Assumptions
- LiteLLM is used for the automation opportunity scoring rationale, but since we cannot connect to a real PM4PY Python backend natively without complex setup in this autonomous session, the process discovery algorithms (BPMN generation, variant detection) will be simulated or rule-based within the Spring Boot service to satisfy the acceptance criteria.
- Multitenancy is handled via `X-Tenant-ID` header as per ecosystem standards.

## Future Work
- Integrate with real Python PM4Py backend via gRPC or REST for true algorithmic process mining.
## Missing/Future Integrations
- pgmq async job processing: The 202 Accepted endpoints currently execute synchronously. Future iterations should offload these to a pgmq worker.
- Test coverage: Service layer test coverage is currently basic and needs to be expanded to hit the >80% threshold.
- Docker build context for the backend requires access to `cc-starter`, which might need a parent pom or multistage build adjusting to include the entire monorepo if built strictly isolated without local .m2 caching.
