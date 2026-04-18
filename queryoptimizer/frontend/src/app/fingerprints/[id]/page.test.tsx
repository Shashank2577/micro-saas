import { vi, describe, it, expect } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import FingerprintDetail from './page';

// Mock params and api
vi.mock('next/navigation', () => ({
  useParams: () => ({ id: '123' })
}));

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn().mockImplementation((url) => {
      if (url.includes('baseline')) {
        return Promise.resolve({ data: { averageExecutionTimeMs: 120, executionCount: 5 } });
      }
      return Promise.resolve({ data: { normalizedQuery: 'SELECT * FROM test' } });
    }),
    post: vi.fn().mockResolvedValue({})
  }
}));

describe('FingerprintDetail Page', () => {
  it('renders components and fetches data', async () => {
    render(<FingerprintDetail />);
    
    expect(screen.getByText('Loading...')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('Fingerprint Details')).toBeInTheDocument();
      expect(screen.getByText('SELECT * FROM test')).toBeInTheDocument();
      expect(screen.getByText('120.00 ms')).toBeInTheDocument();
      expect(screen.getByText('Execution Plan Visualization')).toBeInTheDocument();
    });
  });
});
