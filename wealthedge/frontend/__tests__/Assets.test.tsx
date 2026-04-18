import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';
import AssetsPage from '../src/app/assets/page';
import api from '../src/lib/api';

vi.mock('../src/lib/api', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
  },
}));

describe('AssetsPage', () => {
  it('fetches and displays assets', async () => {
    (api.get as any).mockResolvedValue({
      data: [
        { id: '1', name: 'House', type: 'REAL_ESTATE', currentValue: 500000 }
      ]
    });

    render(<AssetsPage />);
    
    await waitFor(() => {
      expect(screen.getByText('House')).toBeInTheDocument();
      expect(screen.getByText('$500,000')).toBeInTheDocument();
    });
  });
});
