import { render, screen } from '@testing-library/react';
import Dashboard from '@/app/page';

describe('Dashboard', () => {
  it('renders dashboard correctly', () => {
    render(<Dashboard />);
    expect(screen.getByText('CallIntelligence Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Total Calls Analyzed')).toBeInTheDocument();
  });
});
