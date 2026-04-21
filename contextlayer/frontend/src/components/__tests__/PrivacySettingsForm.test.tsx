import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { PrivacySettingsForm } from '../PrivacySettingsForm';

// Mock fetch
global.fetch = vi.fn() as any;

describe('PrivacySettingsForm', () => {
  it('renders buttons correctly', () => {
    render(<PrivacySettingsForm customerId="cust_1" />);
    expect(screen.getByText('Opt-in to Marketing')).toBeInTheDocument();
    expect(screen.getByText('Opt-out of Marketing')).toBeInTheDocument();
  });

  it('calls fetch when opt-in is clicked', async () => {
    (global.fetch as any).mockResolvedValueOnce({ ok: true });

    render(<PrivacySettingsForm customerId="cust_1" />);
    const btn = screen.getByText('Opt-in to Marketing');
    fireEvent.click(btn);

    expect(global.fetch).toHaveBeenCalledWith('/api/customers/cust_1/consent', expect.any(Object));
  });
});
