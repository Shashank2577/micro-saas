import '@testing-library/jest-dom/vitest';
import { vi } from 'vitest';

global.React = require('react');

vi.mock('react-chartjs-2', () => ({
  Line: () => null,
  Bar: () => null
}));
