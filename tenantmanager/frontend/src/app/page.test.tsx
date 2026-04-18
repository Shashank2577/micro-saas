import { render, screen, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import DashboardPage from './page';

vi.mock('../lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [] }),
    post: vi.fn(),
  }
}));

describe('DashboardPage', () => {
  it('renders dashboard heading', async () => {
    await act(async () => {
      render(<DashboardPage />);
    });
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
  });
});
