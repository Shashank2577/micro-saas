import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { Dashboard } from '../components/Dashboard';
import { describe, it, expect } from 'vitest';

describe('Dashboard Component', () => {
  it('renders anomalies correctly', () => {
    const anomalies = [
      { id: '1', anomalyType: 'UNUSUAL_TIME', severity: 'HIGH', status: 'OPEN' }
    ];
    render(<Dashboard anomalies={anomalies} reviews={[]} />);
    expect(screen.getByText('HIGH')).toBeInTheDocument();
  });

  it('renders reviews correctly', () => {
    const reviews = [
      { id: '1', status: 'PENDING', aiRecommendation: 'REVOKE all' }
    ];
    render(<Dashboard anomalies={[]} reviews={reviews} />);
    expect(screen.getByText('PENDING - AI says: REVOKE all')).toBeInTheDocument();
  });
});
