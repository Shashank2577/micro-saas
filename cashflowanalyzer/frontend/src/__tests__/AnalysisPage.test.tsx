import { render, screen, waitFor } from '@testing-library/react';
import AnalysisPage from '../app/analysis/page';
import * as api from '../lib/api';
import { vi } from 'vitest';

vi.mock('../lib/api');

describe('AnalysisPage', () => {
    it('renders loading state initially', () => {
        vi.mocked(api.fetchMovements).mockReturnValue(new Promise(() => {}));
        render(<AnalysisPage />);
        expect(screen.getByTestId('loading')).toBeInTheDocument();
    });

    it('renders data after fetch', async () => {
        vi.mocked(api.fetchMovements).mockResolvedValue([
            { id: '1', tenantId: 't1', name: 'Movement 1', status: 'CLEARED' }
        ]);
        render(<AnalysisPage />);
        await waitFor(() => {
            expect(screen.getByText('Movement 1')).toBeInTheDocument();
        });
    });
});
