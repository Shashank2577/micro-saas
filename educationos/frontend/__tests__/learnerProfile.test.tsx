import { render, screen } from '@testing-library/react';
import LearnerProfilePage from '../src/app/learners/profile/page';
import { vi } from 'vitest';

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: { backgroundInfo: 'Dev', learningStyle: 'VISUAL' } }),
    post: vi.fn().mockResolvedValue({}),
  },
}));

describe('Learner Profile Page', () => {
  it('renders heading', () => {
    render(<LearnerProfilePage />);
    expect(screen.getByText('Learner Profile')).toBeInTheDocument();
  });
});
