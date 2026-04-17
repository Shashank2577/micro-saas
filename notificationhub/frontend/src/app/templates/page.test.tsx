import { render, screen } from '@testing-library/react';
import { test, expect } from 'vitest';
import TemplatesPage from './page';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

test('renders templates page', () => {
  render(
    <QueryClientProvider client={queryClient}>
        <TemplatesPage />
    </QueryClientProvider>
  );
  expect(screen.getByText('Loading templates...')).toBeDefined();
});
