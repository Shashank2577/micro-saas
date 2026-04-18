import { render, screen } from '@testing-library/react';
import { StatCard } from '../../components/StatCard';
import { describe, it, expect } from 'vitest';

describe('StatCard', () => {
  it('renders correctly', () => {
    render(<StatCard title="Hits" value="100" subtitle="Total hits" />);
    
    expect(screen.getByText('Hits')).toBeInTheDocument();
    expect(screen.getByText('100')).toBeInTheDocument();
    expect(screen.getByText('Total hits')).toBeInTheDocument();
  });
});
