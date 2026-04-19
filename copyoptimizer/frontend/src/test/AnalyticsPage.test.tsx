import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import AnalyticsPage from '../app/analytics/page';

describe('AnalyticsPage', () => {
  it('renders stats', () => {
    render(<AnalyticsPage />);

    expect(screen.getByText('Analytics Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Variants Generated')).toBeInTheDocument();
    expect(screen.getByText('120')).toBeInTheDocument(); // matches mock data
  });
});
