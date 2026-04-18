import '@testing-library/jest-dom/vitest';
import * as React from 'react';

// Required for some testing libraries with Vite
global.React = React;

// Mock next/navigation
vi.mock('next/navigation', () => ({
  useRouter: () => ({
    push: vi.fn(),
    back: vi.fn(),
  }),
  useSearchParams: () => new URLSearchParams({ propertyId: '123' }),
  useParams: () => ({ id: '123' }),
}));
