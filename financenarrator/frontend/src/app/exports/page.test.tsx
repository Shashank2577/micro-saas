import { describe, it, expect } from "vitest";
import { render, screen } from '@testing-library/react';
import ExportsPage from './page';
import * as api from '../../lib/api/exports';
import { vi } from 'vitest';

vi.mock('../../lib/api/exports', () => ({
  fetchExportArtifacts: vi.fn().mockResolvedValue([
    { id: '1', name: 'Export 1', status: 'DRAFT' }
  ])
}));

describe('ExportsPage', () => {
  it('renders correctly', async () => {
    render(<ExportsPage />);
    expect(await screen.findByText('Export Artifacts')).toBeInTheDocument();
    expect(await screen.findByText('Export 1')).toBeInTheDocument();
  });
});
