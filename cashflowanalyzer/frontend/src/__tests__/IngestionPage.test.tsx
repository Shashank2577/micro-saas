import { render, screen, waitFor } from '@testing-library/react';
import IngestionPage from '../app/ingestion/page';
import * as api from '../lib/api';
import { vi } from 'vitest';

vi.mock('../lib/api');

describe('IngestionPage', () => {
    it('renders loading state initially', () => {
        vi.mocked(api.fetchPeriods).mockReturnValue(new Promise(() => {}));
        render(<IngestionPage />);
        expect(screen.getByTestId('loading')).toBeInTheDocument();
    });

    it('renders data after fetch', async () => {
        vi.mocked(api.fetchPeriods).mockResolvedValue([
            { id: '1', tenantId: 't1', name: 'Period 1', status: 'ACTIVE' }
        ]);
        render(<IngestionPage />);
        await waitFor(() => {
            expect(screen.getByText('Period 1')).toBeInTheDocument();
        });
    });
});
