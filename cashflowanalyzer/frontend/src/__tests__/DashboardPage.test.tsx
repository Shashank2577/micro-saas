import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import DashboardPage from '../app/dashboard/page';
import React from 'react';

// Mock fetch
global.fetch = vi.fn(() =>
  Promise.resolve({
    json: () => Promise.resolve({ totalIncome: 1000, totalExpenses: 500, savingsRate: 0.5 }),
  })
) as any;

describe('DashboardPage', () => {
  it('renders correctly', async () => {
    render(<DashboardPage />);
    expect(screen.getByText('Total Income')).toBeDefined();
    expect(screen.getByText('Total Expenses')).toBeDefined();
    expect(screen.getByText('Savings Rate')).toBeDefined();
  });
});
