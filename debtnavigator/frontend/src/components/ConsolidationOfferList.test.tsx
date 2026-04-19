import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import ConsolidationOfferList from './ConsolidationOfferList';

vi.mock('@/lib/api', () => ({
  apiFetch: vi.fn().mockResolvedValue([
    { id: '1', name: 'Test Offer', status: 'ACTIVE' }
  ])
}));

describe('ConsolidationOfferList', () => {
  it('renders consolidation offers from api', async () => {
    render(<ConsolidationOfferList />);
    await waitFor(() => {
      expect(screen.getByText('Test Offer - ACTIVE')).toBeInTheDocument();
    });
  });
});
