import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { PolicyList } from '../PolicyList';
import { api } from '../../lib/api';
import React from 'react';

vi.mock('../../lib/api', () => ({
  api: {
    policies: { list: vi.fn().mockResolvedValue([]) }
  }
}));

describe('PolicyList', () => {
  it('renders "No policies found." initially or when empty', async () => {
    render(<PolicyList />);
    expect(await screen.findByText('No policies found.')).toBeInTheDocument();
  });
});
