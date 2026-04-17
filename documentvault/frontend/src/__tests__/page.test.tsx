import { render, screen, waitFor } from '@testing-library/react';
import DocumentVaultDashboard from '../app/page';
import { vi, describe, it, expect, beforeEach } from 'vitest';

vi.mock('../lib/api', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    delete: vi.fn()
  }
}));

import api from '../lib/api';

describe('DocumentVaultDashboard', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders correctly and loads empty state', async () => {
    (api.get as any).mockResolvedValueOnce({ data: { content: [] } });
    
    render(<DocumentVaultDashboard />);
    
    expect(screen.getByText('Document Vault')).toBeInDocument();
    
    await waitFor(() => {
      expect(screen.getByText('No documents found. Upload a file to get started.')).toBeInDocument();
    });
  });

  it('renders documents from API', async () => {
    (api.get as any).mockResolvedValueOnce({
      data: {
        content: [
          { id: '1', title: 'test.pdf', sizeBytes: 1024, createdAt: '2023-01-01T00:00:00.000Z' }
        ]
      }
    });

    render(<DocumentVaultDashboard />);

    await waitFor(() => {
      expect(screen.getByText('test.pdf')).toBeInDocument();
    });
  });
});
