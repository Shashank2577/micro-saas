import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import DashboardPage from '../page';

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [] }),
    post: vi.fn().mockResolvedValue({ data: {} })
  }
}));

describe('DashboardPage', () => {
  it('renders dashboard correctly', () => {
    render(<DashboardPage />);
    expect(screen.getByText('TelemetryCore Dashboard')).toBeInTheDocument();
    expect(screen.getByTestId('active-users-count')).toBeInTheDocument();
  });
});
