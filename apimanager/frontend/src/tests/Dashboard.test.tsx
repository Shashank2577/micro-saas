import { render, screen, waitFor } from '@testing-library/react';
import React from 'react';
import Dashboard from '../app/page';
import { api } from '../lib/api';
import { vi } from 'vitest';

vi.mock('../lib/api', () => ({
  api: {
    get: vi.fn(),
    post: vi.fn()
  }
}));

describe('Dashboard', () => {
  it('shows projects list', async () => {
    (api.get as any).mockResolvedValue({ data: [{ id: '1', name: 'Test Project', description: 'Test desc' }] });
    
    render(<Dashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('Test Project')).toBeInTheDocument();
    });
  });
});
