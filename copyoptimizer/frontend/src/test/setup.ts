import '@testing-library/jest-dom/vitest';
import { cleanup } from '@testing-library/react';
import { afterEach } from 'vitest';

afterEach(() => {
  cleanup();
});

import { vi } from 'vitest';
global.fetch = vi.fn(() => Promise.resolve({ ok: true, json: () => Promise.resolve([ { id: '1', name: 'Mock Data', status: 'OK' } ]) })) as any;
