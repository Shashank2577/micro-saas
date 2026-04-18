import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Home from './page';

describe('Home Page', () => {
  it('renders dashboard overview', () => {
    render(<Home />);
    expect(screen.getByText('Dashboard Overview')).toBeInTheDocument();
    expect(screen.getByText('ProcessMiner')).toBeInTheDocument();
    expect(screen.getByText('Total Processes Analyzed')).toBeInTheDocument();
  });
});
