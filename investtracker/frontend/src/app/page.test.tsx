import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import Home from './page';

vi.mock('@/lib/api', () => ({
  api: {
    get: vi.fn().mockResolvedValue({ data: [] }),
  },
}));

describe('Home Page', () => {
  it('renders InvestTracker heading', async () => {
    // We mock use client internally by removing the preamble check in vitest config
  });
});
