import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import CustomersPage from '../app/customers/page';
import { api } from '@/lib/api';

vi.mock('@/lib/api', () => ({
  api: {
    get: vi.fn(),
  },
}));

describe('CustomersPage', () => {
  it('renders customers successfully', async () => {
    const mockCustomers = [
      { id: '1', name: 'Acme Corp', industry: 'Tech', mrr: 1000, latestHealthScore: { compositeScore: 85 }, latestPrediction: { riskSegment: 'LOW' } }
    ];
    (api.get as any).mockResolvedValue({ data: mockCustomers });

    render(<CustomersPage />);
    
    await waitFor(() => {
      expect(screen.getByText('Acme Corp')).toBeInTheDocument();
      expect(screen.getByText('Tech')).toBeInTheDocument();
      expect(screen.getByText('$1000')).toBeInTheDocument();
      expect(screen.getByText('85')).toBeInTheDocument();
      expect(screen.getByText('LOW')).toBeInTheDocument();
    });
  });
});
