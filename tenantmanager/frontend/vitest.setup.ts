import '@testing-library/jest-dom';
import { vi } from 'vitest';
import React from 'react';

// Bypass React preamble issue
(global as any).React = React;

vi.mock('next/navigation', () => ({
  useParams: () => ({ id: '123' }),
  useRouter: () => ({
    push: vi.fn(),
    replace: vi.fn(),
    prefetch: vi.fn(),
  }),
  useSearchParams: () => new URLSearchParams(),
}));
