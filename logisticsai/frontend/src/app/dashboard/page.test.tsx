import { render, screen, waitFor } from '@testing-library/react';
import Dashboard from './page';
import { vi } from 'vitest';

vi.mock('@/lib/api', () => ({
  fetchApi: vi.fn(() => Promise.resolve([
    { id: '1', carrierName: 'Test Carrier', onTimeRate: 98.5, predictedDelayMins: 5 }
  ]))
}));

describe('Dashboard Component', () => {
  it('renders correctly and loads data', async () => {
    render(<Dashboard />);
    expect(screen.getByText('Carrier Performance Dashboard')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('Test Carrier')).toBeInTheDocument();
      expect(screen.getByText('98.5%')).toBeInTheDocument();
      expect(screen.getByText('5 mins')).toBeInTheDocument();
    });
  });
});
