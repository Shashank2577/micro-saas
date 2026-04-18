import { render, screen } from '@testing-library/react';
import Dashboard from './page';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

describe('Dashboard', () => {
  it('renders Dashboard heading', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <Dashboard />
      </QueryClientProvider>
    );
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Total Annual Forecast')).toBeInTheDocument();
  });
});
