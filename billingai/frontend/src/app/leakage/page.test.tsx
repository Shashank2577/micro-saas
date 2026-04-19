import React from 'react';
import { render, screen } from '@testing-library/react';
import LeakagePage from './page';

describe('LeakagePage', () => {
  it('renders the heading', () => {
    render(<LeakagePage />);
    expect(screen.getByTestId('leakage-heading')).toBeInTheDocument();
  });
});
