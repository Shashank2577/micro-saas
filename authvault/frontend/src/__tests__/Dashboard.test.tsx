import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import DashboardPage from '../app/dashboard/page';

describe('DashboardPage', () => {
    it('renders dashboard overview', () => {
        render(<DashboardPage />);
        expect(screen.getByText('AuthVault Dashboard')).toBeInTheDocument();
        expect(screen.getByText('Users')).toBeInTheDocument();
        expect(screen.getByText('Roles & Permissions')).toBeInTheDocument();
        expect(screen.getByText('OAuth Apps')).toBeInTheDocument();
    });
});
