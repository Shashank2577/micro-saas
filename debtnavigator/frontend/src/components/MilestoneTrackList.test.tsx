import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import MilestoneTrackList from './MilestoneTrackList';

vi.mock('@/lib/api', () => ({
  apiFetch: vi.fn().mockResolvedValue([
    { id: '1', name: 'Test Milestone', status: 'ACTIVE' }
  ])
}));

describe('MilestoneTrackList', () => {
  it('renders milestone tracks from api', async () => {
    render(<MilestoneTrackList />);
    await waitFor(() => {
      expect(screen.getByText('Test Milestone - ACTIVE')).toBeInTheDocument();
    });
  });
});
