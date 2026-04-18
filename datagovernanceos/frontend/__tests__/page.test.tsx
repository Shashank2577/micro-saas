import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import DashboardPage from '../src/app/page';

vi.mock('../src/lib/api', () => ({
  api: {
    assets: {
      list: vi.fn().mockResolvedValue([
        { id: '1', name: 'Customer Database', type: 'DATABASE', classification: 'CONFIDENTIAL', piiStatus: true }
      ])
    },
    policies: {
      list: vi.fn().mockResolvedValue([
        { id: '1', name: 'PII Protection Policy' }
      ])
    },
    audits: {
      list: vi.fn().mockResolvedValue([
        { id: '1', status: 'FAILED' }
      ])
    }
  }
}));

describe('DashboardPage', () => {
  it('renders the dashboard with stats and assets table', async () => {
    const jsx = await DashboardPage();
    render(jsx);

    await waitFor(() => {
      expect(screen.getByText('DataGovernanceOS Dashboard')).toBeInTheDocument();
      expect(screen.getByText('Customer Database')).toBeInTheDocument();
      expect(screen.getByText('CONFIDENTIAL')).toBeInTheDocument();
    });
  });
});
