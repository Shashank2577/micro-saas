# Handoff Notes

## Assumptions
- Base schema for all entities consists of `id`, `tenant_id`, `name`, `status`, `metadata_json`, `created_at`, `updated_at`.
- Validation and Simulation endpoints are mocks that just return success payloads for the purpose of the prototype.
- Standard Spring Boot structure and React Next.js structure applied.
- AI implementation will just wrap a prompt to the AiService.
