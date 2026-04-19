import { render, screen } from '@testing-library/react';
import { expect, test, vi } from 'vitest';
import MarketDatasetsPage from '../page';

global.fetch = vi.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve([{ id: '1', name: 'Dataset A', status: 'ACTIVE', createdAt: '2025-01-01' }]),
  })
) as any;

test('renders MarketDatasets page with fetched data', async () => {
  render(<MarketDatasetsPage />);
  expect(screen.getByText('Market Datasets')).toBeDefined();
  const datasetName = await screen.findByText('Dataset A');
  expect(datasetName).toBeDefined();
});
