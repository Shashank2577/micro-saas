import { render, screen, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import DashboardPage from './page';
import React from 'react';
import { act } from '@testing-library/react';

// Mock the global fetch
global.fetch = vi.fn();

describe('DashboardPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders dashboard with deals from API', async () => {
    const mockDeals = [
      { id: '1', name: 'Acme Corp Deal', amount: 50000, stage: 'NEGOTIATION' }
    ];

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockDeals,
    });

    await act(async () => {
      render(<DashboardPage />);
    });

    expect(screen.getByText('Pipeline Dashboard')).toBeInTheDocument();

    // Wait for the mock deal to be rendered
    await waitFor(() => {
      expect(screen.getByText('Acme Corp Deal')).toBeInTheDocument();
    });

    expect(screen.getByText('$50,000')).toBeInTheDocument();
    expect(screen.getByText(/NEGOTIATION/)).toBeInTheDocument();
  });

  it('renders empty state when no deals exist', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    await act(async () => {
      render(<DashboardPage />);
    });

    await waitFor(() => {
      expect(screen.getByText('No deals found.')).toBeInTheDocument();
    });
  });
});
