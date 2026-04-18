import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import RiskProjectionList from './RiskProjectionList';

vi.mock('@/lib/api', () => ({
  apiFetch: vi.fn().mockResolvedValue([
    { id: '1', name: 'Test Projection', status: 'ACTIVE' }
  ])
}));

describe('RiskProjectionList', () => {
  it('renders risk projections from api', async () => {
    render(<RiskProjectionList />);
    await waitFor(() => {
      expect(screen.getByText('Test Projection - ACTIVE')).toBeInTheDocument();
    });
  });
});
