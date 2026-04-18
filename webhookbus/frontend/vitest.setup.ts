import '@testing-library/jest-dom/vitest';
import React from 'react';

(global as any).React = React;

// Mock next/link
vi.mock('next/link', () => ({
  default: ({ children }: any) => React.createElement('a', {}, children)
}));

// Mock ResizeObserver
global.ResizeObserver = class ResizeObserver {
  observe() {}
  unobserve() {}
  disconnect() {}
};
