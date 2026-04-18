import { render, screen, waitFor } from '@testing-library/react';
import ExceptionsPage from './page';
import { vi } from 'vitest';

vi.mock('@/lib/api', () => ({
  fetchApi: vi.fn(() => Promise.resolve([
    { id: '1', description: 'Test issue', severity: 'HIGH', recommendedAction: 'Action', status: 'OPEN' }
  ]))
}));

describe('ExceptionsPage Component', () => {
  it('renders correctly', async () => {
    render(<ExceptionsPage />);
    expect(screen.getByText('AI Exception Agent')).toBeInTheDocument();
  });
});
