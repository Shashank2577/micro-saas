import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Dashboard from '../page';

describe('Dashboard', () => {
  it('renders the dashboard heading', () => {
    render(<Dashboard />);
    expect(screen.getByText('EcosystemMap Dashboard')).toBeInTheDocument();
  });
});
