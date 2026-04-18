import { render, screen } from '@testing-library/react';
import MarketplaceHubHome from '../page';
import { vi, describe, it, expect } from 'vitest';

vi.mock('../../lib/api', () => ({
  fetchApps: vi.fn().mockResolvedValue([{ id: '1', name: 'WealthEdge', description: 'Wealth management app', category: 'Finance' }]),
  fetchTrendingApps: vi.fn().mockResolvedValue([{ id: '2', name: 'InvestPro', description: 'Investing app' }])
}));

describe('MarketplaceHubHome', () => {
  it('renders correctly', async () => {
    render(<MarketplaceHubHome />);
    expect(screen.getByText('MarketplaceHub')).toBeInTheDocument();
  });
});
