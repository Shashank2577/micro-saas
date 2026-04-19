# Handoff Notes

## Assumptions Made
1. Events in the spec refer to the `ProcurementEvents` entity. This includes REST endpoints for events.
2. Tenant scope is hardcoded as `00000000-0000-0000-0000-000000000001` for the MVP phase in controllers.
3. Added simple frontend pages structure.

## Future work
- Integrate real user tenant IDs via Auth tokens.
- Add E2E tests using cypress or playwright.
- Add detailed validations and business logic inside the generic `validate` endpoints.
