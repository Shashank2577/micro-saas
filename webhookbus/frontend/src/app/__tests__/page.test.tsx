import { vi, describe, it, expect, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import DashboardPage from '../page';
import { api } from '../../lib/api';
import React from 'react';

vi.mock('../../lib/api', () => ({
  api: {
    endpoints: { list: vi.fn() },
    deliveries: { list: vi.fn() },
  }
}));

describe('DashboardPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders loading state initially', async () => {
    (api.endpoints.list as any).mockResolvedValue([]);
    (api.deliveries.list as any).mockResolvedValue({ content: [] });

    render(React.createElement(DashboardPage));
    expect(screen.getByText('Loading deliveries...')).toBeInTheDocument();
  });

  it('renders stats and deliveries after loading', async () => {
    (api.endpoints.list as any).mockResolvedValue([
      { id: '1', status: 'ACTIVE' },
      { id: '2', status: 'INACTIVE' }
    ]);
    (api.deliveries.list as any).mockResolvedValue({
      content: [
        { id: '1', status: 'SUCCESS', event: { eventType: 'test.event' }, endpoint: { name: 'Test Endpoint' }, createdAt: '2023-01-01T00:00:00Z' }
      ]
    });

    render(React.createElement(DashboardPage));

    await waitFor(() => {
      expect(screen.queryByText('Loading deliveries...')).not.toBeInTheDocument();
    });

    // Check stats: 1 active endpoint, 1 success delivery
    expect(screen.getAllByText('1')).toHaveLength(2); // Active endpoints and Successful deliveries
    
    // Check delivery table
    expect(screen.getByText('test.event')).toBeInTheDocument();
    expect(screen.getByText('Test Endpoint')).toBeInTheDocument();
    expect(screen.getByText('SUCCESS')).toBeInTheDocument();
  });
});
