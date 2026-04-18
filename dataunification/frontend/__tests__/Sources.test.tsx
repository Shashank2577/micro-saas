import { render, screen } from '@testing-library/react';
import Sources from '../src/app/sources/page';
import { describe, it, expect, vi } from 'vitest';

vi.mock('../src/lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [] }),
    post: vi.fn().mockResolvedValue({ data: {} })
  }
}));

describe('Sources Page', () => {
  it('renders heading and add button', () => {
    render(<Sources />);
    expect(screen.getByText('Data Sources')).toBeInTheDocument();
    expect(screen.getByText('Add Mock Source')).toBeInTheDocument();
  });
});
