import React from 'react';
import { render, screen } from '@testing-library/react';
import TaxPage from './page';

describe('TaxPage', () => {
  it('renders the heading', () => {
    render(<TaxPage />);
    expect(screen.getByTestId('tax-heading')).toBeInTheDocument();
  });
});
