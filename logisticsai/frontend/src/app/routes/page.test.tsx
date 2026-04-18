import { render, screen, waitFor } from '@testing-library/react';
import RoutesPage from './page';
import { vi } from 'vitest';

vi.mock('@/lib/api', () => ({
  fetchApi: vi.fn(() => Promise.resolve([
    { id: '1', origin: 'NY', destination: 'LA', status: 'PLANNED', estimatedArrival: null }
  ]))
}));

describe('RoutesPage Component', () => {
  it('renders correctly', async () => {
    render(<RoutesPage />);
    expect(screen.getByText('Route Optimizer')).toBeInTheDocument();
  });
});
