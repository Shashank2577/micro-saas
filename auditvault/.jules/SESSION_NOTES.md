# Session Notes - AuditVault

- Setup .jules folder.
- Scaffolded app via script.
- Replaced nexus-hub components and dependencies with auditvault references.
- Faced some challenges resolving `cc-starter` dependencies in maven, had to install `cc-starter` via `mvn install` in the repo locally first.
- Fixed spring boot context errors by using isolated test config mocking `cc-starter` beans.
- Vitest configuration set up correctly for Next.js frontend with jsdom.
