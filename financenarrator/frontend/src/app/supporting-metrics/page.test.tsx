import { describe, it, expect } from "vitest";
import { render, screen } from '@testing-library/react';
import SupportingMetricsPage from './page';
import * as api from '../../lib/api/supporting-metrics';
import { vi } from 'vitest';

vi.mock('../../lib/api/supporting-metrics', () => ({
  fetchSupportingMetrics: vi.fn().mockResolvedValue([
    { id: '1', name: 'Metric 1', status: 'DRAFT' }
  ])
}));

describe('SupportingMetricsPage', () => {
  it('renders correctly', async () => {
    render(<SupportingMetricsPage />);
    expect(await screen.findByText('Supporting Metrics')).toBeInTheDocument();
    expect(await screen.findByText('Metric 1')).toBeInTheDocument();
  });
});
