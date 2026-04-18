import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import AnalysisPage from '../app/analysis/page';
import api from '../lib/api';

vi.mock('../lib/api', () => {
  return {
    default: {
      post: vi.fn(),
    },
  };
});

describe('AnalysisPage Component', () => {
  it('renders correctly and submits trace id', async () => {
    (api.post as any).mockResolvedValue({ data: { rootCause: "Database timeout" } });

    render(<AnalysisPage />);
    
    expect(screen.getByText('AI Root Cause Analysis')).toBeInTheDocument();
    
    const input = screen.getByPlaceholderText('Enter Trace ID...');
    fireEvent.change(input, { target: { value: 'trace-123' } });
    
    const button = screen.getByRole('button', { name: 'Analyze Trace' });
    fireEvent.click(button);
    
    await waitFor(() => {
      expect(api.post).toHaveBeenCalledWith('/api/analyze', { traceId: 'trace-123' });
    });
  });
});
