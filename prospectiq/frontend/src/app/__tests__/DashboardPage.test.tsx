import { render, screen, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import DashboardPage from '../page';

vi.mock('@/lib/api', () => ({
  api: {
    prospects: {
      list: vi.fn().mockResolvedValue([
        { id: '1', name: 'Acme Corp', industry: 'Tech' }
      ])
    }
  }
}));

describe('DashboardPage', () => {
  it('renders title and stats', async () => {
    await act(async () => {
        render(<DashboardPage />);
    });
    expect(screen.getByText('ProspectIQ Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Signal Coverage Stats')).toBeInTheDocument();
  });
});
