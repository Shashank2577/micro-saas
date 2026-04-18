import { vi, describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import Home from './page';

describe('Home Page', () => {
  it('renders dashboard metrics', () => {
    render(<Home />);
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Total Queries')).toBeInTheDocument();
    expect(screen.getByText('Optimizations Found')).toBeInTheDocument();
  });
});
