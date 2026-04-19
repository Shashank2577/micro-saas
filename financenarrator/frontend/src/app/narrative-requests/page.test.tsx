import { describe, it, expect } from "vitest";
import { render, screen } from '@testing-library/react';
import NarrativeRequestsPage from './page';
import * as api from '../../lib/api/narrative-requests';
import { vi } from 'vitest';

vi.mock('../../lib/api/narrative-requests', () => ({
  fetchNarrativeRequests: vi.fn().mockResolvedValue([
    { id: '1', name: 'Request 1', status: 'DRAFT' }
  ])
}));

describe('NarrativeRequestsPage', () => {
  it('renders correctly', async () => {
    render(<NarrativeRequestsPage />);
    expect(await screen.findByText('Narrative Requests')).toBeInTheDocument();
    expect(await screen.findByText('Request 1')).toBeInTheDocument();
  });
});
