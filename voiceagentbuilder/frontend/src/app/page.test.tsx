import { vi, describe, it, expect, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import DashboardPage from './page';

vi.mock('../lib/api', () => ({
  default: {
    get: vi.fn().mockImplementation((url) => {
      if (url === '/api/v1/agents') {
        return Promise.resolve({ data: [{ id: '1', name: 'Agent 1', description: 'Desc 1' }] });
      }
      if (url === '/api/v1/calls') {
        return Promise.resolve({ data: [{ id: '1', durationSeconds: 60, cost: 0.05 }] });
      }
      return Promise.resolve({ data: [] });
    })
  }
}));

describe('DashboardPage', () => {
  it('renders stats and agents', async () => {
    render(<DashboardPage />);
    
    await waitFor(() => {
      expect(screen.getByText('Agent 1')).toBeInTheDocument();
      expect(screen.getByText('Desc 1')).toBeInTheDocument();
      
      // Total Agents is 1
      const agentsCount = screen.getAllByText('1');
      expect(agentsCount.length).toBeGreaterThan(0);
    });
  });
});
