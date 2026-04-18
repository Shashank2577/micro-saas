import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Home from './page';

describe('Home Page', () => {
  it('renders the title', () => {
    render(<Home />);
    expect(screen.getByText('Restaurant Intel')).toBeInTheDocument();
  });

  it('renders links to all modules', () => {
    render(<Home />);
    expect(screen.getByText('Menu Analysis')).toBeInTheDocument();
    expect(screen.getByText('Predictive Ordering')).toBeInTheDocument();
    expect(screen.getByText('Staff Scheduling')).toBeInTheDocument();
    expect(screen.getByText('Review Intelligence')).toBeInTheDocument();
  });
});
