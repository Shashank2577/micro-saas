import { render, screen, act } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';
import Dashboard from '../src/app/page';

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [] }),
  },
}));

describe('Dashboard', () => {
  it('renders correctly', async () => {
    await act(async () => {
      render(<Dashboard />);
    });
    expect(screen.getByText('RealEstateIntel Dashboard')).toBeInTheDocument();
  });
});
