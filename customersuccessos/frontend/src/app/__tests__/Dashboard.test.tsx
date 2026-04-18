import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import Dashboard from '../page';
import api from '../../lib/api';

vi.mock('../../lib/api');

describe('Dashboard', () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it('renders stats from API', async () => {
    (api.get as any).mockImplementation((url: string) => {
      if (url === '/api/accounts') return Promise.resolve({ data: [{ id: '1' }, { id: '2' }] });
      if (url === '/api/accounts/expansion/all') return Promise.resolve({ data: [{ id: '1' }] });
      return Promise.resolve({ data: [] });
    });

    render(<Dashboard />);
    
    await waitFor(() => {
      expect(screen.getByTestId('accounts-count')).toHaveTextContent('2');
      expect(screen.getByTestId('opps-count')).toHaveTextContent('1');
    });
  });
});
