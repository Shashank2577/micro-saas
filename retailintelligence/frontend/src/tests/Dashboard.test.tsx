import { render, screen, waitFor } from '@testing-library/react';
import Dashboard from '../app/page';
import api from '../lib/api';
import { vi, describe, it, expect } from 'vitest';

vi.mock('../lib/api');

describe('Dashboard Component', () => {
  it('renders dashboard and fetches metrics', async () => {
    (api.get as any).mockImplementation((url: string) => {
      if (url === '/api/skus') return Promise.resolve({ data: [{ id: '1' }, { id: '2' }] });
      if (url === '/api/pricing-recommendations') return Promise.resolve({ data: [{ id: '1' }] });
      return Promise.reject(new Error('not found'));
    });

    render(<Dashboard />);

    expect(screen.getByText('RetailIntelligence Dashboard')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('2')).toBeInTheDocument(); // Total SKUs
      expect(screen.getByText('1')).toBeInTheDocument(); // Pending actions
    });
  });
});
