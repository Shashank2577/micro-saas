import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import DashboardPage from '../app/page';

// Mock Recharts to avoid jsdom issues
vi.mock('recharts', () => ({
  ResponsiveContainer: ({ children }: any) => <div>{children}</div>,
  AreaChart: () => <div data-testid="area-chart"></div>,
  Area: () => <div></div>,
  XAxis: () => <div></div>,
  YAxis: () => <div></div>,
  CartesianGrid: () => <div></div>,
  Tooltip: () => <div></div>,
}));

describe('DashboardPage', () => {
  it('renders loading state initially or stats if loaded', () => {
    render(<DashboardPage />);
    expect(screen.getByText(/Loading...|ChurnPredictor Dashboard/)).toBeInTheDocument();
  });
});
