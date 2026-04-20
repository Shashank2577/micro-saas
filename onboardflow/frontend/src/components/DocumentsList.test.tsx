import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import DocumentsList from './DocumentsList';
import { fetchApi } from '../lib/api';
import { describe, it, expect, vi } from 'vitest';

vi.mock('../lib/api', () => ({
  fetchApi: vi.fn(),
}));

describe('DocumentsList', () => {
  it('renders loading state initially', () => {
    vi.mocked(fetchApi).mockReturnValue(new Promise(() => {}));
    render(<DocumentsList />);
    expect(screen.getByText('Loading Documents...')).toBeDefined();
  });

  it('renders data when loaded', async () => {
    const mockData = [{ id: '1', name: 'Test 1', status: 'ACTIVE' }];
    vi.mocked(fetchApi).mockResolvedValue(mockData);

    render(<DocumentsList />);

    await waitFor(() => {
      expect(screen.getByText('Test 1')).toBeDefined();
      expect(screen.getByText('ACTIVE')).toBeDefined();
    });
  });
});
