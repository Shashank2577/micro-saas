import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import CalibrationSignalPage from './page';

describe('Calibration Signals Page', () => {
    it('renders heading', async () => {
        render(<CalibrationSignalPage />);
        expect(screen.getByText('Calibration Signals')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByTestId('calibration-table')).toBeInTheDocument();
        });

        expect(screen.getByText('Test Calibration Signals')).toBeInTheDocument();
    });
});
