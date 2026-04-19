import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import CandidateList from '../CandidateList';

describe('CandidateList', () => {
  it('renders "No candidates found." initially or when empty', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([]),
      })
    ) as any;

    render(<CandidateList />);
    expect(screen.getByText('No candidates found.')).toBeInTheDocument();
  });

  it('renders a list of candidates when data is present', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          { id: '1', name: 'John Doe', status: 'Active' },
          { id: '2', name: 'Jane Smith', status: 'Hired' }
        ]),
      })
    ) as any;

    render(<CandidateList />);
    await waitFor(() => {
      expect(screen.getByText('John Doe - Active')).toBeInTheDocument();
      expect(screen.getByText('Jane Smith - Hired')).toBeInTheDocument();
    });
  });
});
