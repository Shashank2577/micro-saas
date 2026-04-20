import { render, screen } from '@testing-library/react';
import SegmentsPage from '../page';
import { vi, describe, it, expect, beforeEach } from 'vitest';

global.fetch = vi.fn();

describe('SegmentsPage', () => {
  beforeEach(() => {
    (global.fetch as any).mockResolvedValue({
      json: () => Promise.resolve([
        { id: '1', name: 'Segment 1', size: 100 },
      ]),
    });
  });

  it('renders segments title', async () => {
    render(<SegmentsPage />);
    expect(screen.getByText('Customer Segments & Elasticity')).toBeInTheDocument();
  });
});
