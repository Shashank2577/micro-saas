import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import AccountList from './AccountList';

vi.mock('@/lib/api', () => ({
  apiFetch: vi.fn().mockResolvedValue([
    { id: '1', name: 'Test Account', status: 'ACTIVE' }
  ])
}));

describe('AccountList', () => {
  it('renders accounts from api', async () => {
    render(<AccountList />);
    await waitFor(() => {
      expect(screen.getByText('Test Account - ACTIVE')).toBeInTheDocument();
    });
  });
});
