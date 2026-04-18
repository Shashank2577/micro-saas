import '@testing-library/jest-dom/vitest';
import React from 'react';

(global as any).React = React;

import { vi } from 'vitest';

vi.mock('react-chartjs-2', () => ({
  Pie: () => null,
  Bar: () => null,
  Doughnut: () => null
}));
