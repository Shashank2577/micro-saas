## Questions Resolved During Build
- Q: Should HRIS sync be real-time or batch?
  A: Spec mentions "bulk employee data sync". Implemented as a POST endpoint that can be used for batch uploads.
- Q: Which PDF library to use?
  A: Spec mentions iText. Used iText 7.
- Q: How to handle multi-tenancy in background jobs?
  A: Pulse survey processing payload includes tenantId to restore TenantContext.

## Assumptions
- Multi-tenancy: Assumed X-Tenant-ID header is always provided by the gateway for authenticated requests.
- PII: Encrypted at rest using cc-starter's EncryptionService.
- AI Models: Defaulting to claude-3-5-sonnet via LiteLLM.

## Future Work
- [ ] Add more granular pulse survey question types.
- [ ] Implement deeper predictive models using historical trends (currently basic scoring).
- [ ] Add departmental drill-down in the frontend dashboard.
