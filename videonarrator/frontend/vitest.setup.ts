import '@testing-library/jest-dom';
import { vi } from 'vitest';
import React from 'react';

// Make React globally available for the tests
global.React = React;

// Mock next/navigation
vi.mock('next/navigation', () => ({
  useRouter: () => ({
    push: vi.fn(),
    replace: vi.fn(),
    prefetch: vi.fn(),
  }),
  useParams: () => ({}),
  useSearchParams: () => new URLSearchParams(),
}));

// Mock next/image
vi.mock('next/image', () => ({
  default: (props: any) => React.createElement('img', props)
}));
