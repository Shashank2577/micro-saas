import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import PlanList from './PlanList';

vi.mock('@/lib/api', () => ({
  apiFetch: vi.fn().mockResolvedValue([
    { id: '1', name: 'Test Plan', status: 'ACTIVE' }
  ])
}));

describe('PlanList', () => {
  it('renders plans from api', async () => {
    render(<PlanList />);
    await waitFor(() => {
      expect(screen.getByText('Test Plan - ACTIVE')).toBeInTheDocument();
    });
  });
});
