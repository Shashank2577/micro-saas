import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import VideoPlayer from './VideoPlayer';

// Mock video.js so it doesn't crash in jsdom
vi.mock('video.js', () => ({
  default: vi.fn(() => ({
    on: vi.fn(),
    dispose: vi.fn(),
    currentTime: vi.fn(() => 0)
  }))
}));

describe('VideoPlayer', () => {
  it('renders video element', () => {
    render(<VideoPlayer src="test.mp4" />);
    // Testing implementation details is tricky due to videojs mocking,
    // so we just verify it doesn't crash and renders the wrapper.
    const wrapper = document.querySelector('[data-vjs-player]');
    expect(wrapper).toBeInTheDocument();
  });
});
