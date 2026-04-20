import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import OnboardingTasksList from './OnboardingTasksList';
import { fetchApi } from '../lib/api';
import { describe, it, expect, vi } from 'vitest';

vi.mock('../lib/api', () => ({
  fetchApi: vi.fn(),
}));

describe('OnboardingTasksList', () => {
  it('renders loading state initially', () => {
    vi.mocked(fetchApi).mockReturnValue(new Promise(() => {}));
    render(<OnboardingTasksList />);
    expect(screen.getByText('Loading OnboardingTasks...')).toBeDefined();
  });

  it('renders data when loaded', async () => {
    const mockData = [{ id: '1', name: 'Test 1', status: 'ACTIVE' }];
    vi.mocked(fetchApi).mockResolvedValue(mockData);

    render(<OnboardingTasksList />);

    await waitFor(() => {
      expect(screen.getByText('Test 1')).toBeDefined();
      expect(screen.getByText('ACTIVE')).toBeDefined();
    });
  });
});
