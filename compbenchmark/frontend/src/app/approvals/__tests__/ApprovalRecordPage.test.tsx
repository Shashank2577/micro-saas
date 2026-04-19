import { render, screen } from '@testing-library/react';
import { expect, test, vi } from 'vitest';
import ApprovalRecordsPage from '../page';

global.fetch = vi.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve([{ id: '1', name: 'Record A', status: 'ACTIVE', createdAt: '2025-01-01' }]),
  })
) as any;

test('renders ApprovalRecords page with fetched data', async () => {
  render(<ApprovalRecordsPage />);
  expect(screen.getByText('Approval Records')).toBeDefined();
  const recordName = await screen.findByText('Record A');
  expect(recordName).toBeDefined();
});
