import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import OpportunitiesPage from '../opportunities/page';
import { api } from '@/lib/api';

vi.mock('@/lib/api', () => ({
  api: {
    get: vi.fn().mockResolvedValue({ data: [] }),
    post: vi.fn().mockResolvedValue({ data: [{ id: '1', sourceApp: 'AppA', targetApp: 'AppB', description: 'desc', potentialValue: 'val' }] })
  }
}));

describe('OpportunitiesPage', () => {
  it('renders and fetches analysis', async () => {
    render(<OpportunitiesPage />);
    expect(screen.getByText('Loading opportunities...')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText('Integration Opportunities')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByText('Run AI Analysis'));
    await waitFor(() => {
      expect(screen.getByText('AppA → AppB')).toBeInTheDocument();
    });
  });
});
