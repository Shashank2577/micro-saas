import '@testing-library/jest-dom/vitest';
import { vi } from 'vitest';
import React from 'react';

global.React = React;

vi.mock('next/navigation', () => ({
  useRouter: () => ({
    push: vi.fn(),
    back: vi.fn(),
  }),
  useParams: () => ({
    id: '1',
  }),
}));
