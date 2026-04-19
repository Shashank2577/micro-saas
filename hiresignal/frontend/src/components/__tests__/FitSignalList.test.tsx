import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import FitSignalList from '../FitSignalList';

describe('FitSignalList', () => {
  it('renders "No fit signals found." initially or when empty', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([]),
      })
    ) as any;

    render(<FitSignalList />);
    expect(screen.getByText('No fit signals found.')).toBeInTheDocument();
  });

  it('renders a list of fit signals when data is present', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          { id: '1', name: 'Signal 1', status: 'Active' },
        ]),
      })
    ) as any;

    render(<FitSignalList />);
    await waitFor(() => {
      expect(screen.getByText('Signal 1 - Active')).toBeInTheDocument();
    });
  });
});
