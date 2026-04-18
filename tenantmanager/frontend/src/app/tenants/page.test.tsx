import { render, screen, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import TenantsPage from './page';

vi.mock('../../lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [] }),
    post: vi.fn(),
  }
}));

describe('TenantsPage', () => {
  it('renders tenants heading', async () => {
    await act(async () => {
      render(<TenantsPage />);
    });
    expect(screen.getByText('Tenants')).toBeInTheDocument();
  });
});
