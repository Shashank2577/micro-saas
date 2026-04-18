import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import NewSubscriptionPage from '../app/subscriptions/new/page';

// Mock next/navigation
vi.mock('next/navigation', () => ({
  useRouter() {
    return {
      push: vi.fn(),
    };
  },
}));

describe('NewSubscriptionPage', () => {
  it('renders form and handles input', () => {
    render(<NewSubscriptionPage />);
    const input = screen.getByTestId('plan-input');
    fireEvent.change(input, { target: { value: 'plan_123' } });
    expect((input as HTMLInputElement).value).toBe('plan_123');
  });
});
