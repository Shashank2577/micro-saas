import '@testing-library/jest-dom/vitest';
import { vi } from 'vitest';

// Mock Plotly.js to prevent loading errors in jsdom
vi.mock('react-plotly.js', () => ({
  default: () => null
}));
