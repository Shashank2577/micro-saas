import { render, screen, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import DashboardPage from '../page';
import api from '../../lib/api';

vi.mock('../../lib/api', () => ({
  default: {
    get: vi.fn(),
  }
}));

describe('DashboardPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders loading state initially', () => {
    (api.get as any).mockImplementation(() => new Promise(() => {}));
    render(<DashboardPage />);
    expect(screen.getByText('Loading dashboard...')).toBeInTheDocument();
  });

  it('renders dashboard content after loading', async () => {
    (api.get as any).mockImplementation((url: string) => {
      if (url === '/events') {
        return Promise.resolve({ data: [{ id: '1', createdAt: '2024-01-01T00:00:00Z', userId: 'user1', eventName: 'login', properties: {} }] });
      }
      if (url.includes('/metrics')) {
        return Promise.resolve({ data: [{ id: '1', metricName: 'DAU', value: 100, createdAt: '2024-01-01T00:00:00Z' }] });
      }
      return Promise.resolve({ data: [] });
    });

    render(<DashboardPage />);

    await waitFor(() => {
      expect(screen.getByText('Usage Intelligence Dashboard')).toBeInTheDocument();
    });

    expect(screen.getByText('1')).toBeInTheDocument(); // 1 event
    expect(screen.getByTestId('mock-line-chart')).toBeInTheDocument();
  });
});
