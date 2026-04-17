import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import CompetitorList from '../components/CompetitorList';
import * as api from '../lib/api';

vi.mock('../lib/api', () => ({
  fetchCompetitors: vi.fn(),
  addCompetitor: vi.fn(),
  deleteCompetitor: vi.fn()
}));

describe('CompetitorList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders and allows adding/removing competitors', async () => {
    const mockCompetitors = [{ id: '1', name: 'Acme Corp' }];
    vi.mocked(api.fetchCompetitors).mockResolvedValue(mockCompetitors);
    vi.mocked(api.addCompetitor).mockResolvedValue({ id: '2', name: 'Beta Inc' });

    render(<CompetitorList />);

    await waitFor(() => {
      expect(screen.getByText('Acme Corp')).toBeInTheDocument();
    });

    const input = screen.getByPlaceholderText('New Competitor Name');
    fireEvent.change(input, { target: { value: 'Beta Inc' } });

    const addButton = screen.getByText('Add Competitor');
    fireEvent.click(addButton);

    expect(api.addCompetitor).toHaveBeenCalledWith({ name: 'Beta Inc' });
  });
});
