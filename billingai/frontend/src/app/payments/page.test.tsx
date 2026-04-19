import React from 'react';
import { render, screen } from '@testing-library/react';
import PaymentsPage from './page';

describe('PaymentsPage', () => {
  it('renders the heading', () => {
    render(<PaymentsPage />);
    expect(screen.getByTestId('payments-heading')).toBeInTheDocument();
  });
});
