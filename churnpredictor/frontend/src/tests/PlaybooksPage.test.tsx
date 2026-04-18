import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import PlaybooksPage from '../app/playbooks/page';
import { api } from '@/lib/api';

vi.mock('@/lib/api', () => ({
  api: {
    get: vi.fn(),
  },
}));

describe('PlaybooksPage', () => {
  it('renders playbooks successfully', async () => {
    const mockPlaybooks = [
      { id: '1', name: 'High Risk Alert', description: 'Alert for high risk', triggerRiskSegment: 'HIGH', actionType: 'EMAIL', active: true }
    ];
    (api.get as any).mockResolvedValue({ data: mockPlaybooks });

    render(<PlaybooksPage />);
    
    await waitFor(() => {
      expect(screen.getByText('High Risk Alert')).toBeInTheDocument();
      expect(screen.getByText('Alert for high risk')).toBeInTheDocument();
      expect(screen.getByText('HIGH')).toBeInTheDocument();
      expect(screen.getByText('EMAIL')).toBeInTheDocument();
      expect(screen.getByText('Active')).toBeInTheDocument();
    });
  });
});
