import React from 'react';
import { render, screen } from '@testing-library/react';
import DunningPage from './page';

describe('DunningPage', () => {
  it('renders the heading', () => {
    render(<DunningPage />);
    expect(screen.getByTestId('dunning-heading')).toBeInTheDocument();
  });
});
