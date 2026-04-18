import { render, screen, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import React from 'react';
import WorkspaceSettings from './page';
import api from '../lib/api';

vi.mock('../lib/api', () => ({
  default: {
    get: vi.fn(),
    put: vi.fn()
  }
}));

describe('WorkspaceSettings', () => {
  it('renders loading state initially', async () => {
    (api.get as any).mockResolvedValueOnce({ data: null });
    await act(async () => {
      render(<WorkspaceSettings />);
    });
    expect(screen.getByText('No workspace found. Run backend with initialization.')).toBeInTheDocument();
  });
});
