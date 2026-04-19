import { render, screen } from '@testing-library/react';
import { expect, test, vi } from 'vitest';
import GapFindingsPage from '../page';

global.fetch = vi.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve([{ id: '1', name: 'Gap A', status: 'ACTIVE', createdAt: '2025-01-01' }]),
  })
) as any;

test('renders GapFindings page with fetched data', async () => {
  render(<GapFindingsPage />);
  expect(screen.getByText('Gap Findings')).toBeDefined();
  const gapName = await screen.findByText('Gap A');
  expect(gapName).toBeDefined();
});
