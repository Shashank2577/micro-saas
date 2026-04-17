import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import RecorderPage from './page';
import { api } from '../../lib/api';

// Mock the API
jest.mock('../../lib/api', () => ({
  api: {
    transcribe: jest.fn().mockResolvedValue({
      id: 'enc-123',
      transcript: 'Test transcription',
    }),
  },
}));

describe('RecorderPage', () => {
  it('handles recording and transcription', async () => {
    jest.useFakeTimers();
    render(<RecorderPage />);

    const button = screen.getByText('Start Recording');
    fireEvent.click(button);

    expect(screen.getByText('Recording...')).toBeInTheDocument();

    // Fast-forward the timeout
    jest.advanceTimersByTime(2000);

    await waitFor(() => {
      expect(screen.getByText('Start Recording')).toBeInTheDocument();
    });

    expect(screen.getByText('Test transcription')).toBeInTheDocument();
    expect(screen.getByText(/enc-123/)).toBeInTheDocument();

    jest.useRealTimers();
  });
});
