## Questions Resolved During Build
- Q: Did not find specific error code responses or authentication details for endpoints in the spec.
  A: Assumed standard REST practices and bypassed mock authentication via `excludeAutoConfiguration = SecurityAutoConfiguration.class` in test controllers for isolation.
  
## Assumptions
- LiteLLM calls return predefined structured JSON and fallback correctly during tests.
- Uses fixed Mock user ID (`123e4567-e89b-12d3-a456-426614174000`) for the current iteration since no Auth/Session layer was requested for the frontend.

## Future Work
- [ ] Implement end-to-end user authentication with Keycloak.
- [ ] Connect with genuine real-time external healthcare data estimation sources.
- [ ] Implement robust error boundaries and display custom messages in frontend.
