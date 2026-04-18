import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import PricingPage from '../app/pricing/page';
import api from '../lib/api';
import { vi, describe, it, expect } from 'vitest';

vi.mock('../lib/api');

describe('Pricing Page', () => {
  it('renders recommendations and handles apply', async () => {
    const mockRecommendations = [
      { id: 'rec-1', skuId: 'sku-1', recommendedPrice: 29.99, reasoning: 'Competitor dip', marginPercentage: 25.5 }
    ];

    (api.get as any).mockResolvedValue({ data: mockRecommendations });
    (api.put as any).mockResolvedValue({});

    render(<PricingPage />);

    await waitFor(() => {
      expect(screen.getByText(/Competitor dip/)).toBeInTheDocument();
    });

    const applyBtn = screen.getByText('Apply Price');
    fireEvent.click(applyBtn);

    await waitFor(() => {
      expect(api.put).toHaveBeenCalledWith('/api/pricing-recommendations/rec-1/apply');
    });
  });
});
