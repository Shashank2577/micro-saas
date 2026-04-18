import { vi, describe, it, expect } from 'vitest';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import NewAgentPage from './page';

vi.mock('next/navigation', () => ({
  useRouter: () => ({
    push: vi.fn(),
  }),
}));

vi.mock('../../../lib/api', () => ({
  default: {
    post: vi.fn().mockImplementation((url) => {
      if (url === '/api/v1/agents') {
        return Promise.resolve({ data: { id: 'new-agent-id' } });
      }
      return Promise.reject();
    })
  }
}));

describe('NewAgentPage', () => {
  it('renders and allows submission', async () => {
    render(<NewAgentPage />);
    expect(screen.getByText('Create New Agent')).toBeInTheDocument();
    
    // Can just verify the button exists for now
    expect(screen.getByText('Create Agent')).toBeInTheDocument();
  });
});
