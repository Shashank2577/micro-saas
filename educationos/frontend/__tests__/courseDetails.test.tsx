import { render, screen } from '@testing-library/react';
import CourseDetailsPage from '../src/app/courses/[id]/page';
import { vi } from 'vitest';

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn().mockImplementation((url) => {
      if (url.includes('/modules')) {
        return Promise.resolve({ data: [] });
      }
      return Promise.resolve({ data: { id: '1', title: 'Course 1', description: 'Desc 1' } });
    }),
  },
}));

describe('Course Details Page', () => {
  it('renders loading state initially', () => {
    render(<CourseDetailsPage />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });
});
