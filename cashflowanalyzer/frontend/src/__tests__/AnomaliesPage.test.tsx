import { render, screen, waitFor } from '@testing-library/react';
import AnomaliesPage from '../app/anomalies/page';
import * as api from '../lib/api';
import { vi } from 'vitest';

vi.mock('../lib/api');

describe('AnomaliesPage', () => {
    it('renders loading state initially', () => {
        vi.mocked(api.fetchAnomalies).mockReturnValue(new Promise(() => {}));
        render(<AnomaliesPage />);
        expect(screen.getByTestId('loading')).toBeInTheDocument();
    });

    it('renders data after fetch', async () => {
        vi.mocked(api.fetchAnomalies).mockResolvedValue([
            { id: '1', tenantId: 't1', name: 'Anomaly 1', status: 'OPEN' }
        ]);
        render(<AnomaliesPage />);
        await waitFor(() => {
            expect(screen.getByText('Anomaly 1')).toBeInTheDocument();
        });
    });
});
