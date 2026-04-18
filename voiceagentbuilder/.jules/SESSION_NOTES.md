# Session Notes

- Maven compilation issues: Resolved by adding parent `voiceagentbuilder-parent` root pom.xml to allow `-pl backend` functionality.
- Vitest configuration issues: Fixed preamble issue by dropping the `@vitejs/plugin-react` inline config that Next.js doesn't like, and injecting `global.React = React` in `vitest.setup.ts`.
- Next.js build errors: Resolved by deleting default template `AppCard.tsx` that referenced missing `EcosystemApp` type. Next config migrated to `.mjs`.
- Spring Context loading errors: Added exclusions for all cc-starter auto configurations (`com.crosscutting.starter.*`) to prevent database/message broker connection failures during testing.
