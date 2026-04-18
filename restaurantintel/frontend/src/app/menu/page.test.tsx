import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import MenuAnalysis from './page';
import api from '../../lib/api';

vi.mock('../../lib/api', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn()
  }
}));

describe('MenuAnalysis Page', () => {
  it('renders loading initially and then items', async () => {
    const mockItems = [
      { id: '1', name: 'Burger', category: 'Main', profitMargin: 5.0, unitsSold: 100 }
    ];
    (api.get as any).mockResolvedValueOnce({ data: mockItems });

    render(<MenuAnalysis />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText('Menu Intelligence')).toBeInTheDocument();
    });

    expect(screen.getByText('Burger')).toBeInTheDocument();
  });
});
