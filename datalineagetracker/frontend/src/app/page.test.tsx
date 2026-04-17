import { render, screen } from '@testing-library/react';
import Home from './page';

// Mock chart.js so it doesn't fail in jsdom
vi.mock('react-chartjs-2', () => ({
  Doughnut: () => <div>Mocked Doughnut Chart</div>
}));

describe('Home Page', () => {
  it('renders the dashboard components', () => {
    render(<Home />);
    
    expect(screen.getByText('Data Lineage Tracker')).toBeInTheDocument();
    expect(screen.getByText('Data Assets')).toBeInTheDocument();
    expect(screen.getByText('Lineage Graph')).toBeInTheDocument();
    expect(screen.getByText('Governance')).toBeInTheDocument();
    expect(screen.getByText('Audit Trails')).toBeInTheDocument();
    expect(screen.getByText('Compliance Readiness')).toBeInTheDocument();
    expect(screen.getByText('Overall Data Governance Health')).toBeInTheDocument();
  });
});
