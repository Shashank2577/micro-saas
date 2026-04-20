import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import TransactionsPage from '../app/transactions/page';
import React from 'react';

// Mock fetch
global.fetch = vi.fn(() =>
  Promise.resolve({
    json: () => Promise.resolve([{ id: '1', date: '2023-01-01', name: 'Coffee', amount: 5 }]),
  })
) as any;

describe('TransactionsPage', () => {
  it('renders correctly', async () => {
    render(<TransactionsPage />);
    expect(screen.getByText('Transactions')).toBeDefined();
  });
});
