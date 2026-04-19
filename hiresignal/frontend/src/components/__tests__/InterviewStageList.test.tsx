import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import InterviewStageList from '../InterviewStageList';

describe('InterviewStageList', () => {
  it('renders "No interview stages found." initially or when empty', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([]),
      })
    ) as any;

    render(<InterviewStageList />);
    expect(screen.getByText('No interview stages found.')).toBeInTheDocument();
  });

  it('renders a list of interview stages when data is present', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          { id: '1', name: 'Stage 1', status: 'Active' },
        ]),
      })
    ) as any;

    render(<InterviewStageList />);
    await waitFor(() => {
      expect(screen.getByText('Stage 1 - Active')).toBeInTheDocument();
    });
  });
});
