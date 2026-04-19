import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { ApiSpecList } from './ApiSpecList';
import '@testing-library/jest-dom';

vi.mock('swr', () => ({
  default: () => ({ data: [], error: null, isLoading: false })
}));

describe('ApiSpecList', () => {
  it('renders correctly', () => {
    render(<ApiSpecList />);
    expect(screen.getByText('Create New Spec')).toBeInTheDocument();
  });
});
