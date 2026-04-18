import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';

vi.mock('../../lib/api', () => ({
  api: {
    get: vi.fn().mockResolvedValue([
      { id: '1', claimNumber: 'CLM-001', policyNumber: 'POL-001', status: 'NEW', amount: 100 }
    ])
  }
}));

import ClaimsPage from './page';

describe('ClaimsPage', () => {
  it('renders claims list', async () => {
    render(<ClaimsPage />);
    expect(await screen.findByText(/CLM-001/)).toBeInTheDocument();
  });
});
