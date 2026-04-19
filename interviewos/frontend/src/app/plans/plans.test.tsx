import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import InterviewPlanPage from './page';

describe('Interview Plans Page', () => {
    it('renders heading', async () => {
        render(<InterviewPlanPage />);
        expect(screen.getByText('Interview Plans')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByTestId('plans-table')).toBeInTheDocument();
        });

        expect(screen.getByText('Test Interview Plans')).toBeInTheDocument();
    });
});
