import { render, screen } from '@testing-library/react';
import EmployeesPage from './page';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

describe('EmployeesPage', () => {
  it('renders correctly', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <EmployeesPage />
      </QueryClientProvider>
    );
    expect(screen.getByText('Employees')).toBeInTheDocument();
  });
});
