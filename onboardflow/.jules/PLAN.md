# Implementation Plan

1. **Backend Foundation**
   - Use python scripts to generate entities, repositories, dtos, controllers and services.
   - Refactor backend generated files if necessary.
   - Add backend tests.

2. **Frontend Foundation**
   - Use python script to generate frontend components.
   - Refactor frontend files if necessary.
   - Add frontend tests.

3. **Verify and Adjust**
   - Ensure `mvn clean verify` passes in backend.
   - Ensure `npm run build` and `npm test` passes in frontend.
   - Fix all issues encountered.
