import '@testing-library/jest-dom/vitest';
import React from 'react';
import { vi } from 'vitest';

global.React = React;

// mock next router
vi.mock('next/navigation', () => ({
  useRouter: () => ({
    push: vi.fn(),
    replace: vi.fn(),
    prefetch: vi.fn(),
  }),
  usePathname: () => '/',
}));
