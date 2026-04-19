import { render, screen, waitFor } from '@testing-library/react';
import ForecastingPage from '../app/forecasting/page';
import * as api from '../lib/api';
import { vi } from 'vitest';

vi.mock('../lib/api');

describe('ForecastingPage', () => {
    it('renders loading state initially', () => {
        vi.mocked(api.fetchForecasts).mockReturnValue(new Promise(() => {}));
        render(<ForecastingPage />);
        expect(screen.getByTestId('loading')).toBeInTheDocument();
    });

    it('renders data after fetch', async () => {
        vi.mocked(api.fetchForecasts).mockResolvedValue([
            { id: '1', tenantId: 't1', name: 'Forecast 1', status: 'COMPLETED' }
        ]);
        render(<ForecastingPage />);
        await waitFor(() => {
            expect(screen.getByText('Forecast 1')).toBeInTheDocument();
        });
    });
});
