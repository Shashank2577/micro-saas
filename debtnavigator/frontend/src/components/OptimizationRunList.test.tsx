import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import OptimizationRunList from './OptimizationRunList';

vi.mock('@/lib/api', () => ({
  apiFetch: vi.fn().mockResolvedValue([
    { id: '1', name: 'Test Run', status: 'ACTIVE' }
  ])
}));

describe('OptimizationRunList', () => {
  it('renders optimization runs from api', async () => {
    render(<OptimizationRunList />);
    await waitFor(() => {
      expect(screen.getByText('Test Run - ACTIVE')).toBeInTheDocument();
    });
  });
});
