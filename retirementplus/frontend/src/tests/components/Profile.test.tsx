import { vi, describe, it, expect } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import ProfilePage from '../../app/profile/page';

vi.mock('next/navigation', () => ({
  useRouter() {
    return {
      push: vi.fn(),
    };
  },
}));

vi.mock('../../lib/api', () => ({
  default: {
    post: vi.fn().mockResolvedValue({ data: {} }),
  },
}));

describe('Profile Page', () => {
  it('renders profile form', () => {
    render(<ProfilePage />);
    expect(screen.getByText(/Retirement Profile/i)).toBeInTheDocument();
    expect(screen.getByText(/Current Age/i)).toBeInTheDocument();
  });

  it('allows user to type in form', () => {
    const { container } = render(<ProfilePage />);
    const input = container.querySelector('input[name="currentAge"]') as HTMLInputElement;
    fireEvent.change(input, { target: { value: '62' } });
    expect(input.value).toBe('62');
  });
});
