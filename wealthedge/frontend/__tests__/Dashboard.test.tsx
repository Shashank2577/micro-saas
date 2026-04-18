import { render, screen, waitFor } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';
import Dashboard from '../src/app/page';
import api from '../src/lib/api';

vi.mock('../src/lib/api', () => ({
  default: {
    get: vi.fn(),
  },
}));

describe('Dashboard', () => {
  it('renders total net worth and portfolios', async () => {
    (api.get as any).mockResolvedValue({
      data: [
        { id: '1', name: 'Main', totalValue: 1000000 },
        { id: '2', name: 'Real Estate', totalValue: 500000 }
      ]
    });

    render(<Dashboard />);
    
    expect(screen.getByText('WealthEdge Dashboard')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('$1,500,000')).toBeInTheDocument();
    });
  });
});
