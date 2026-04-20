import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import TicketsPage from '../app/tickets/page';

describe('TicketsPage', () => {
    it('renders the tickets table', () => {
        render(<TicketsPage />);
        expect(screen.getByRole('heading', { name: /Support Tickets/i })).toBeDefined();
        expect(screen.getByText('ZD-100')).toBeDefined();
        expect(screen.getByText('Login Issue')).toBeDefined();
    });
});
