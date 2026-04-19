import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, act } from '@testing-library/react';
import DocumentsPage from '../app/documents/page';
import api from '../lib/api';

vi.mock('../lib/api', () => ({
  default: {
    get: vi.fn()
  }
}));

describe('DocumentsPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders loading state initially', async () => {
    (api.get as any).mockResolvedValue({ data: [] });
    await act(async () => {
      render(<DocumentsPage />);
    });
    expect(screen.getByText('All Documents')).toBeInTheDocument();
  });

  it('renders a list of documents', async () => {
    const mockDocs = [
      { id: '1', title: 'Test Doc 1', format: 'BLOG_POST', tone: 'PROFESSIONAL', status: 'COMPLETED', createdAt: '2023-01-01T00:00:00Z' },
      { id: '2', title: 'Test Doc 2', format: 'EMAIL', tone: 'CASUAL', status: 'DRAFT', createdAt: '2023-01-02T00:00:00Z' }
    ];
    (api.get as any).mockResolvedValue({ data: mockDocs });

    await act(async () => {
      render(<DocumentsPage />);
    });

    expect(screen.getByText('Test Doc 1')).toBeInTheDocument();
    expect(screen.getByText('Test Doc 2')).toBeInTheDocument();
  });
});
