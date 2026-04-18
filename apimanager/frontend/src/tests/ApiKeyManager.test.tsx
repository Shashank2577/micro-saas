import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import React from 'react';
import ApiKeyManager from '../components/ApiKeyManager';
import { api } from '../lib/api';
import { vi } from 'vitest';

vi.mock('../lib/api', () => ({
  api: {
    get: vi.fn(),
    post: vi.fn(),
    delete: vi.fn()
  }
}));

describe('ApiKeyManager', () => {
  it('renders and fetches keys', async () => {
    (api.get as any).mockResolvedValue({ data: [] });
    
    render(<ApiKeyManager projectId="123" />);
    
    expect(screen.getByText('API Keys')).toBeInTheDocument();
    expect(screen.getByText('Generate New Key')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(api.get).toHaveBeenCalledWith('/api/v1/keys?projectId=123');
    });
  });

  it('displays generated key', async () => {
    (api.get as any).mockResolvedValue({ data: [] });
    (api.post as any).mockResolvedValue({ data: { key: 'secret_key_123' } });
    
    render(<ApiKeyManager projectId="123" />);
    
    fireEvent.click(screen.getByText('Generate New Key'));
    
    await waitFor(() => {
      expect(screen.getByText('secret_key_123')).toBeInTheDocument();
    });
  });
});
