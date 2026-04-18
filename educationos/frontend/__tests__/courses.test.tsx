import { render, screen } from '@testing-library/react';
import CoursesPage from '../src/app/courses/page';
import { vi } from 'vitest';

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: [] }),
  },
}));

describe('Courses Page', () => {
  it('renders heading', () => {
    render(<CoursesPage />);
    expect(screen.getByText('Your Courses')).toBeInTheDocument();
  });
});
