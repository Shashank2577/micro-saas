import { render, screen, waitFor } from '@testing-library/react';
import DashboardPage from '../page';
import { vi, describe, it, expect, beforeEach } from 'vitest';

global.fetch = vi.fn();

describe('DashboardPage', () => {
  beforeEach(() => {
    (global.fetch as any).mockResolvedValue({
      json: () => Promise.resolve([
        { id: '1', name: 'Segment 1' },
        { id: '2', name: 'Segment 2' }
      ]),
    });
  });

  it('renders dashboard title', async () => {
    render(<DashboardPage />);
    expect(screen.getByText('Pricing Intelligence Dashboard')).toBeInTheDocument();
  });

  it('fetches and displays segments count', async () => {
    render(<DashboardPage />);
    await waitFor(() => {
      expect(screen.getByText('2')).toBeInTheDocument();
    });
  });
});
