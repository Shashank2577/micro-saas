import { render, screen, waitFor } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';
import Dashboard from '../page';
import api from '../../lib/api';

vi.mock('../../lib/api');

describe('Dashboard', () => {
  it('renders loading state initially', () => {
    (api.get as any).mockImplementation(() => new Promise(() => {}));
    render(<Dashboard />);
    expect(screen.getByTestId('loading')).toBeInTheDocument();
  });

  it('renders stats after loading', async () => {
    (api.get as any).mockResolvedValue({
      data: {
        totalJobs: 1500,
        pendingJobs: 50,
        runningJobs: 20,
        completedJobs: 1400,
        failedJobs: 25,
        deadLetterJobs: 5,
        successRate: 98.2
      }
    });

    render(<Dashboard />);

    await waitFor(() => {
      expect(screen.getByTestId('stat-Total-Jobs')).toHaveTextContent('1500');
      expect(screen.getByTestId('stat-Success-Rate')).toHaveTextContent('98.2%');
    });
  });
});
