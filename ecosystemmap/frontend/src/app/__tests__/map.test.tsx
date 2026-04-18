import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import MapPage from '../map/page';
import { api } from '@/lib/api';

vi.mock('@/lib/api', () => ({
  api: {
    get: vi.fn().mockResolvedValue({ data: { apps: [{ id: '1', name: 'App1', status: 'ACTIVE' }], flows: [] } })
  }
}));

describe('MapPage', () => {
  it('renders the map page correctly', async () => {
    render(<MapPage />);
    expect(screen.getByText('Loading map data...')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText('Ecosystem View')).toBeInTheDocument();
      expect(screen.getByText('App1 - Status: ACTIVE')).toBeInTheDocument();
    });
  });
});
