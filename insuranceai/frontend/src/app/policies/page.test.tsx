import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';

vi.mock('../../lib/api', () => ({
  api: {
    get: vi.fn().mockResolvedValue([
      { id: '1', policyNumber: 'POL-001', customerName: 'John Doe', policyType: 'AUTO', premiumAmount: 500, startDate: '2023-01-01', endDate: '2024-01-01' }
    ])
  }
}));

import PoliciesPage from './page';

describe('PoliciesPage', () => {
  it('renders policies list', async () => {
    render(<PoliciesPage />);
    expect(await screen.findByText(/POL-001/)).toBeInTheDocument();
  });
});
