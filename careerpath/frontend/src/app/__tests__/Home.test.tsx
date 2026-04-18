import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Home from '../page';

describe('Home Component', () => {
  it('renders the CareerPath title and all links', () => {
    render(<Home />);
    
    expect(screen.getByText('CareerPath')).toBeInTheDocument();
    expect(screen.getByText('Career Roadmap')).toBeInTheDocument();
    expect(screen.getByText('My Skills')).toBeInTheDocument();
    expect(screen.getByText('Skill Gaps')).toBeInTheDocument();
    expect(screen.getByText('Role Recommendations')).toBeInTheDocument();
    expect(screen.getByText('Learning Paths')).toBeInTheDocument();
    expect(screen.getByText('Mentorship')).toBeInTheDocument();
    expect(screen.getByText('Development Plan')).toBeInTheDocument();
  });
});
