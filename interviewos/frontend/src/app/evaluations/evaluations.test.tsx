import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import EvaluationRecordPage from './page';

describe('Evaluation Records Page', () => {
    it('renders heading', async () => {
        render(<EvaluationRecordPage />);
        expect(screen.getByText('Evaluation Records')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByTestId('evaluations-table')).toBeInTheDocument();
        });

        expect(screen.getByText('Test Evaluation Records')).toBeInTheDocument();
    });
});
