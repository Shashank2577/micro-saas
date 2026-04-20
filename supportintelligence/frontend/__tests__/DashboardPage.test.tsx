import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import DashboardPage from '../app/dashboard/page';

describe('DashboardPage', () => {
    it('renders the dashboard metrics', () => {
        render(<DashboardPage />);
        expect(screen.getByRole('heading', { name: /Dashboard/i })).toBeDefined();
        expect(screen.getByText('Avg Resolution Time')).toBeDefined();
        expect(screen.getByText('Tickets Resolved')).toBeDefined();
        expect(screen.getByText('CSAT Score')).toBeDefined();
    });
});
