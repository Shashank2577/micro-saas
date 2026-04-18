import { render, screen, waitFor } from '@testing-library/react';
import Home from './page';
import { vi } from 'vitest';
import { api } from '@/lib/api';

vi.mock('@/lib/api', () => ({
  api: {
    integrations: {
      list: vi.fn(),
    },
  },
}));

describe('Home Dashboard', () => {
  it('renders loading state initially', () => {
    vi.mocked(api.integrations.list).mockResolvedValue([]);
    render(<Home />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  it('renders integrations list', async () => {
    vi.mocked(api.integrations.list).mockResolvedValue([
      { id: '1', name: 'Test Integration', sourceConnectorId: 's1', targetConnectorId: 't1', status: 'ACTIVE', createdAt: new Date().toISOString() }
    ]);
    render(<Home />);
    
    await waitFor(() => {
      expect(screen.getByText('Test Integration')).toBeInTheDocument();
      expect(screen.getByText('Status: ACTIVE')).toBeInTheDocument();
    });
  });
});
