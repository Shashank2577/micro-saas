import { render, screen, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import TenantDetailPage from './page';

vi.mock('../../../lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [] }),
    post: vi.fn(),
  }
}));

describe('TenantDetailPage', () => {
  it('renders without crashing', async () => {
    await act(async () => {
      render(<TenantDetailPage />);
    });
    expect(screen.getByText('Status:')).toBeInTheDocument();
  });
});
