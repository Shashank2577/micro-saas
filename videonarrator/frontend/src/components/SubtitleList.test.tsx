import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import SubtitleList from './SubtitleList';

describe('SubtitleList', () => {
  it('renders subtitles', () => {
    const subtitles = [
      { id: '1', startTimeMs: 0, endTimeMs: 1000, content: 'First line' }
    ];

    render(<SubtitleList subtitles={subtitles} currentTime={500} onUpdateSubtitle={vi.fn()} />);

    expect(screen.getByText('First line')).toBeInTheDocument();
  });
});
