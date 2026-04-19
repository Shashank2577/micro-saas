import React from 'react';
import { render, screen } from '@testing-library/react';
import InvoiceRunsPage from './page';

describe('InvoiceRunsPage', () => {
  it('renders the heading', () => {
    render(<InvoiceRunsPage />);
    expect(screen.getByTestId('invoice-runs-heading')).toBeInTheDocument();
  });
});
