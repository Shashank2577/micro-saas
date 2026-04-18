import { describe, it, expect, vi } from 'vitest';
import { render, screen, act } from '@testing-library/react';
import MetricsPage from '../page';

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [{ id: '1', name: 'API Calls', description: 'desc', aggregationType: 'COUNT' }] }),
  }
}));

describe('MetricsPage', () => {
  it('renders metrics page correctly', async () => {
    await act(async () => {
      render(<MetricsPage />);
    });
    expect(screen.getByText('Metrics Definition')).toBeInTheDocument();
    expect(screen.getByText('Name')).toBeInTheDocument();
  });
});
