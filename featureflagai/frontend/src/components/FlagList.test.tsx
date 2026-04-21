import React from 'react';
import { render, screen } from '@testing-library/react';
import FlagList from './FlagList';
import { describe, it, expect, vi } from 'vitest';

global.fetch = vi.fn(() =>
  Promise.resolve({
    json: () => Promise.resolve([{ id: '1', name: 'New Dashboard', enabled: true, rolloutPct: 50 }])
  })
) as any;

describe('FlagList', () => {
  it('renders correctly and lists mocked flags', async () => {
    render(<FlagList />);
    expect(screen.getByTestId('flag-list')).toBeInTheDocument();
    // Wait for async fetch to resolve
    expect(await screen.findByText('New Dashboard')).toBeInTheDocument();
  });
});
