import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ImpactAnalysis from './ImpactAnalysis';
import { describe, it, expect, vi } from 'vitest';

global.fetch = vi.fn(() =>
  Promise.resolve({
    text: () => Promise.resolve("Flag 'New Dashboard' increased conversion by 3%. No negative effects detected.")
  })
) as any;

describe('ImpactAnalysis', () => {
  it('shows result after clicking run analysis', async () => {
    render(<ImpactAnalysis />);
    const flagInput = screen.getAllByRole('textbox')[0];
    fireEvent.change(flagInput, { target: { value: 'flag-123' } });

    const btn = screen.getByText('Run Analysis');
    fireEvent.click(btn);

    await waitFor(() => {
      expect(screen.getByText(/increased conversion by 3%/)).toBeInTheDocument();
    });
  });
});
