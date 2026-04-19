import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import OnboardingPlanList from './OnboardingPlanList';
describe('OnboardingPlanList', () => {
  it('renders loading state initially', () => {
    vi.spyOn(global, 'fetch').mockImplementation(() => new Promise(() => {}));
    render(<OnboardingPlanList />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });
  it('renders list of plans after fetch', async () => {
    const mockPlans = [{ id: '1', name: 'Plan A', status: 'DRAFT' }];
    vi.spyOn(global, 'fetch').mockResolvedValue({ ok: true, json: () => Promise.resolve(mockPlans) } as Response);
    render(<OnboardingPlanList />);
    await waitFor(() => { expect(screen.getByTestId('onboarding-plan-list')).toBeInTheDocument(); });
    expect(screen.getByText('Plan A - DRAFT')).toBeInTheDocument();
  });
});
