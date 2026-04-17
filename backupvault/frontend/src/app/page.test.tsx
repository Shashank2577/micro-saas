import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import DashboardPage from './page';

describe('Dashboard Page', () => {
  it('renders dashboard heading', () => {
    render(<DashboardPage />);
    expect(screen.getAllByText('Dashboard').length).toBeGreaterThan(0);
  });

  it('renders quick stats', () => {
    render(<DashboardPage />);
    expect(screen.getAllByText('Active Policies').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Recent Executions').length).toBeGreaterThan(0);
  });
});
