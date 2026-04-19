import { render, screen } from '@testing-library/react';
import { expect, test, vi } from 'vitest';
import CompBandsPage from '../page';

// Mock fetch globally for the test
global.fetch = vi.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve([{ id: '1', name: 'Band A', status: 'ACTIVE', createdAt: '2025-01-01' }]),
  })
) as any;

test('renders CompBands page with fetched data', async () => {
  render(<CompBandsPage />);

  // Checks header exists
  expect(screen.getByText('Comp Bands')).toBeDefined();

  // Await data loading
  const bandName = await screen.findByText('Band A');
  expect(bandName).toBeDefined();
});
