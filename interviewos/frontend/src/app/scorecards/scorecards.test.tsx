import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import ScorecardPage from './page';

describe('Scorecards Page', () => {
    it('renders heading', async () => {
        render(<ScorecardPage />);
        expect(screen.getByText('Scorecards')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByTestId('scorecards-table')).toBeInTheDocument();
        });

        expect(screen.getByText('Test Scorecards')).toBeInTheDocument();
    });
});
