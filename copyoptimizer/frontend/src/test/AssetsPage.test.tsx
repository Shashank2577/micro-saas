import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import AssetsPage from '../app/assets/page';

describe('AssetsPage', () => {
  it('renders loading state initially and then data', async () => {
    render(<AssetsPage />);

    // Check if the title is rendered


    // Wait for the stubbed data to "load"
    await waitFor(() => {
      expect(screen.queryByText('Loading...')).not.toBeInTheDocument();
      expect(screen.getByText('Mock Data')).toBeInTheDocument();
    });

    // Check if the mock data row is rendered


  });
});
