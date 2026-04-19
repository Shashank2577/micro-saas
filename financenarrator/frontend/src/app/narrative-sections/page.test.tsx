import { describe, it, expect } from "vitest";
import { render, screen } from '@testing-library/react';
import NarrativeSectionsPage from './page';
import * as api from '../../lib/api/narrative-sections';
import { vi } from 'vitest';

vi.mock('../../lib/api/narrative-sections', () => ({
  fetchNarrativeSections: vi.fn().mockResolvedValue([
    { id: '1', name: 'Section 1', status: 'DRAFT' }
  ])
}));

describe('NarrativeSectionsPage', () => {
  it('renders correctly', async () => {
    render(<NarrativeSectionsPage />);
    expect(await screen.findByText('Narrative Sections')).toBeInTheDocument();
    expect(await screen.findByText('Section 1')).toBeInTheDocument();
  });
});
