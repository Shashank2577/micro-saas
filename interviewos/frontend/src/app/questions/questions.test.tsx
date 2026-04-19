import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import QuestionBankPage from './page';

describe('Question Banks Page', () => {
    it('renders heading', async () => {
        render(<QuestionBankPage />);
        expect(screen.getByText('Question Banks')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByTestId('questions-table')).toBeInTheDocument();
        });

        expect(screen.getByText('Test Question Banks')).toBeInTheDocument();
    });
});
