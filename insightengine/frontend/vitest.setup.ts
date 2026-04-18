import '@testing-library/jest-dom/vitest';
import { vi } from 'vitest';

vi.mock('react-chartjs-2', () => ({
  Line: () => null,
  Bar: () => null,
  Doughnut: () => null
}));
