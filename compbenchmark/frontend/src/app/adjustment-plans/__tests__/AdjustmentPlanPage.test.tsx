import { render, screen } from '@testing-library/react';
import { expect, test, vi } from 'vitest';
import AdjustmentPlansPage from '../page';

global.fetch = vi.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve([{ id: '1', name: 'Plan A', status: 'ACTIVE', createdAt: '2025-01-01' }]),
  })
) as any;

test('renders AdjustmentPlans page with fetched data', async () => {
  render(<AdjustmentPlansPage />);
  expect(screen.getByText('Adjustment Plans')).toBeDefined();
  const planName = await screen.findByText('Plan A');
  expect(planName).toBeDefined();
});
