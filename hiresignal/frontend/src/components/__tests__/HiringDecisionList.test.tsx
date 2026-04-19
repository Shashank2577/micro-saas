import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import HiringDecisionList from '../HiringDecisionList';

describe('HiringDecisionList', () => {
  it('renders "No hiring decisions found." initially or when empty', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([]),
      })
    ) as any;

    render(<HiringDecisionList />);
    expect(screen.getByText('No hiring decisions found.')).toBeInTheDocument();
  });

  it('renders a list of hiring decisions when data is present', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          { id: '1', name: 'Decision 1', status: 'Active' },
        ]),
      })
    ) as any;

    render(<HiringDecisionList />);
    await waitFor(() => {
      expect(screen.getByText('Decision 1 - Active')).toBeInTheDocument();
    });
  });
});
