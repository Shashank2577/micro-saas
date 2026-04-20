import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import TimelineEditor from './TimelineEditor';

describe('TimelineEditor', () => {
  it('renders subtitles in the timeline', () => {
    const subtitles = [
      { id: '1', startTimeMs: 0, endTimeMs: 1000, content: 'Hello' },
      { id: '2', startTimeMs: 1000, endTimeMs: 2000, content: 'World' }
    ];

    render(<TimelineEditor subtitles={subtitles} currentTime={500} />);

    expect(screen.getByText('Hello')).toBeInTheDocument();
    expect(screen.getByText('World')).toBeInTheDocument();
  });
});
