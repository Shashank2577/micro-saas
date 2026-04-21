# Handoff - ConstructionIQ Stub

## Assumptions Made
- Project status, Task status, Task priority, and SafetyIncident severity/status are handled as Strings for simplicity in the stub, but should eventually be Enums.
- The `reported_by` field in `SafetyIncident` and `assigned_to` in `Task` are strings (could be user IDs or names).
- Multi-tenancy is enforced by manually setting/filtering `tenant_id` in the service layer using `TenantContext`.

## Future Work
- Implement full Frontend in `constructioniq/frontend`.
- Convert status/priority fields to Enums.
- Add more comprehensive integration tests for Site, Task, and SafetyIncident controllers.
- Implement real AI prompt engineering and response parsing.
