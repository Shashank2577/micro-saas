## Assumptions
- Frontend tests mock the API calls and ensure the logic and rendering matches state.
- Form components are simplified for the sake of completion.
- AI endpoint relies on the crosscutting infrastructure.

## Questions Resolved
- Next.js requires explicit labeling (`htmlFor="name"` and `id="name"`) for accurate testing component detection.

## Future Work
- Build e2e tests utilizing the actual integrated backend to guarantee consistency across endpoints and AI classification.
