import React from 'react';
import { render, screen } from '@testing-library/react';
import PlansPage from './page';

describe('PlansPage', () => {
  it('renders the heading', () => {
    render(<PlansPage />);
    expect(screen.getByTestId('plans-heading')).toBeInTheDocument();
  });
});
