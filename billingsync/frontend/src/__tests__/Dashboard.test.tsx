import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import DashboardPage from '../app/page';

describe('DashboardPage', () => {
  it('renders the dashboard title and metrics', () => {
    render(<DashboardPage />);
    expect(screen.getByText('BillingSync Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Total MRR')).toBeInTheDocument();
    expect(screen.getByText('Active Subscriptions')).toBeInTheDocument();
  });
});
