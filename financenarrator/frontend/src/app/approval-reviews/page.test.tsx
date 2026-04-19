import { describe, it, expect } from "vitest";
import { render, screen } from '@testing-library/react';
import ApprovalReviewsPage from './page';
import * as api from '../../lib/api/approval-reviews';
import { vi } from 'vitest';

vi.mock('../../lib/api/approval-reviews', () => ({
  fetchApprovalReviews: vi.fn().mockResolvedValue([
    { id: '1', name: 'Review 1', status: 'DRAFT' }
  ])
}));

describe('ApprovalReviewsPage', () => {
  it('renders correctly', async () => {
    render(<ApprovalReviewsPage />);
    expect(await screen.findByText('Approval Reviews')).toBeInTheDocument();
    expect(await screen.findByText('Review 1')).toBeInTheDocument();
  });
});
