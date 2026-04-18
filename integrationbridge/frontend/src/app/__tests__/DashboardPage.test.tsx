import { render, screen, waitFor } from '@testing-library/react';
import DashboardPage from '../page';
import { vi } from 'vitest';

vi.mock('@/lib/api', () => ({
  api: {
    integrations: {
      list: vi.fn().mockResolvedValue([
        { id: '1', provider: 'STRIPE', status: 'HEALTHY', authType: 'OAUTH2', createdAt: new Date().toISOString() }
      ])
    }
  }
}));

describe('DashboardPage', () => {
  it('renders integration list', async () => {
    render(<DashboardPage />);
    expect(screen.getByText('IntegrationBridge Dashboard')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('STRIPE')).toBeInTheDocument();
      expect(screen.getByText('HEALTHY')).toBeInTheDocument();
    });
  });
});
