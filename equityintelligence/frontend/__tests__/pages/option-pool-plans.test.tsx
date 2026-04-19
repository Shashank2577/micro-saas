import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import Page from '../../src/app/dashboard/(equity)/option-pool-plans/page';

// Mock fetch globally
global.fetch = vi.fn();

describe('OptionPoolPlans Page', () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it('renders loading state initially', () => {
    (global.fetch as any).mockImplementationOnce(() => new Promise(() => {}));
    render(<Page />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  it('renders data correctly', async () => {
    const mockData = [
      { id: '1', name: 'Test OptionPoolPlans', status: 'ACTIVE' }
    ];
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockData
    });

    render(<Page />);

    await waitFor(() => {
      expect(screen.getByText('Test OptionPoolPlans')).toBeInTheDocument();
      expect(screen.getByText('ACTIVE')).toBeInTheDocument();
    });
  });

  it('renders error state', async () => {
    (global.fetch as any).mockRejectedValueOnce(new Error('Network error'));

    render(<Page />);

    await waitFor(() => {
      expect(screen.getByText(/Error: Network error/)).toBeInTheDocument();
    });
  });
});
