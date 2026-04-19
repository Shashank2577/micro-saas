import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import RequisitionList from '../RequisitionList';

describe('RequisitionList', () => {
  it('renders "No requisitions found." initially or when empty', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([]),
      })
    ) as any;

    render(<RequisitionList />);
    expect(screen.getByText('No requisitions found.')).toBeInTheDocument();
  });

  it('renders a list of requisitions when data is present', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          { id: '1', name: 'Req 1', status: 'Active' },
        ]),
      })
    ) as any;

    render(<RequisitionList />);
    await waitFor(() => {
      expect(screen.getByText('Req 1 - Active')).toBeInTheDocument();
    });
  });
});
