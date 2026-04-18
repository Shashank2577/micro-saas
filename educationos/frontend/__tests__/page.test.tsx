import { render, screen } from '@testing-library/react';
import Home from '../src/app/page';

describe('Home Page', () => {
  it('renders heading', () => {
    render(<Home />);
    expect(screen.getByText('EducationOS')).toBeInTheDocument();
  });
});
