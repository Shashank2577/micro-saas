import { render, screen } from '@testing-library/react';
import Home from '../app/page';
import { describe, it, expect, vi } from 'vitest';

// Mock the API to avoid fetch errors during test
vi.mock('../lib/api', () => ({
  fetchApi: vi.fn().mockResolvedValue([])
}));

describe('Home Page', () => {
  it('renders heading', () => {
    render(<Home />);
    expect(screen.getByText('MarketSignal Intelligence')).toBeInTheDocument();
  });
});
