import { render, screen, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import VoiceSelector from './VoiceSelector';
import api from '@/lib/api';

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [{ id: 'alloy', name: 'Alloy', provider: 'OPENAI' }] }),
    post: vi.fn()
  }
}));

describe('VoiceSelector', () => {
  it('fetches and renders voices', async () => {
    await act(async () => {
      render(<VoiceSelector projectId="p1" transcriptionId="t1" />);
    });

    const option = await screen.findByText('Alloy');
    expect(option).toBeInTheDocument();
  });
});
