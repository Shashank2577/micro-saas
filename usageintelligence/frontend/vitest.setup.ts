import '@testing-library/jest-dom/vitest';
import React from 'react';

// Make React globally available for the tests
(global as any).React = React;

import { vi } from 'vitest';

vi.mock('react-chartjs-2', () => ({
  Line: () => React.createElement('div', { 'data-testid': 'mock-line-chart' })
}));

// Mock Next.js Link component
vi.mock('next/link', () => ({
  default: ({ children, href }: any) => React.createElement('a', { href }, children)
}));
