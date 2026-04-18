# Session Notes
- Scaffolded nicely. 
- Build issue: cc-starter jar missing in Maven context. Fixing this required stepping into the cross-cutting directory to install it.
- Lombok is extremely flaky in the provided codebase environment - ran into issues with symbol `setActive` on entity/DTO class missing because the maven-compiler-plugin strips out the lombok generation depending on pom hierarchy. Statically generated getters/setters via regex.
- Frontend: Vitest requires `jsdom` setup and a fix inside `package.json`. Tests mocked out the `recharts` responsive containers otherwise the tests fail on ResizeObserver.
- Backend tests require `TenantContext.set(tenantId)` rather than string setting.
