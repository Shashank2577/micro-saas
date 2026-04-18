import { vi, describe, it, expect } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import ProjectionsPage from '../../app/projections/page';

vi.mock('../../lib/api', () => ({
  default: {
    post: vi.fn().mockResolvedValue({
      data: {
        lifeExpectancy: 89,
        safeWithdrawalRate: 4.0,
        socialSecurityClaimingAge: 67,
        estimatedHealthcareCost: 300000,
        qcdOpportunityAge: 72,
        probabilityOfSuccess: 80,
        rothConversionAmount: 50000,
        rmdAmount: 18248,
        stressTestSurvivalRate: 85,
        annuityGuaranteedIncome: 18000,
        taxStrategyOrder: 'Taxable -> Traditional -> Roth'
      }
    }),
  },
}));

describe('Projections Page', () => {
  it('renders loading state initially', () => {
    render(<ProjectionsPage />);
    expect(screen.getByText(/Analyzing your complex retirement scenario/i)).toBeInTheDocument();
  });

  it('renders projection data after loading', async () => {
    render(<ProjectionsPage />);
    await waitFor(() => {
      expect(screen.getByText(/Life Expectancy/i)).toBeInTheDocument();
      expect(screen.getByText(/89/)).toBeInTheDocument();
      expect(screen.getByText(/Roth Conversion/i)).toBeInTheDocument();
      expect(screen.getByText(/50,000/)).toBeInTheDocument();
    });
  });
});
