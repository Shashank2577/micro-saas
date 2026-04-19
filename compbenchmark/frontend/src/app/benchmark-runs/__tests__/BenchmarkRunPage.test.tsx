import { render, screen } from '@testing-library/react';
import { expect, test, vi } from 'vitest';
import BenchmarkRunsPage from '../page';

global.fetch = vi.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve([{ id: '1', name: 'Run A', status: 'ACTIVE', createdAt: '2025-01-01' }]),
  })
) as any;

test('renders BenchmarkRuns page with fetched data', async () => {
  render(<BenchmarkRunsPage />);
  expect(screen.getByText('Benchmark Runs')).toBeDefined();
  const runName = await screen.findByText('Run A');
  expect(runName).toBeDefined();
});
