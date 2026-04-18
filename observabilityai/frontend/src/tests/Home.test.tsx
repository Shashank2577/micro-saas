import { describe, it, expect, vi } from 'vitest';
import { render, screen, act } from '@testing-library/react';
import Home from '../app/page';
import api from '../lib/api';

vi.mock('../lib/api', () => {
  return {
    default: {
      get: vi.fn().mockResolvedValue({ data: [] }),
    },
  };
});

describe('Home Component', () => {
  it('renders dashboard title', async () => {
    await act(async () => {
      render(<Home />);
    });
    expect(screen.getByText('ObservabilityAI Dashboard')).toBeInTheDocument();
  });
});
