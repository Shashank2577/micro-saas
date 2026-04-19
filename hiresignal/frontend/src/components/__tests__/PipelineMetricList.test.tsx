import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import PipelineMetricList from '../PipelineMetricList';

describe('PipelineMetricList', () => {
  it('renders "No pipeline metrics found." initially or when empty', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([]),
      })
    ) as any;

    render(<PipelineMetricList />);
    expect(screen.getByText('No pipeline metrics found.')).toBeInTheDocument();
  });

  it('renders a list of pipeline metrics when data is present', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          { id: '1', name: 'Metric 1', status: 'Active' },
        ]),
      })
    ) as any;

    render(<PipelineMetricList />);
    await waitFor(() => {
      expect(screen.getByText('Metric 1 - Active')).toBeInTheDocument();
    });
  });
});
