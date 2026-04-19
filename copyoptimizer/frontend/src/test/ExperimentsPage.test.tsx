import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import ExperimentsPage from '../app/experiments/page';

describe('ExperimentsPage', () => {
  it('renders loading state initially and then data', async () => {
    render(<ExperimentsPage />);



    await waitFor(() => {
      expect(screen.queryByText('Loading...')).not.toBeInTheDocument();
      expect(screen.getByText('Mock Data')).toBeInTheDocument();
    });


  });
});
