import '@testing-library/jest-dom';
import { vi } from 'vitest';

vi.mock('echarts-for-react', () => ({
  default: () => <div data-testid="echarts-mock" />
}));
