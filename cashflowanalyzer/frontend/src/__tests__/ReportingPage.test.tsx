import { render, screen, waitFor } from '@testing-library/react';
import ReportingPage from '../app/reporting/page';
import * as api from '../lib/api';
import { vi } from 'vitest';

vi.mock('../lib/api');

describe('ReportingPage', () => {
    it('renders loading state initially', () => {
        vi.mocked(api.fetchReporting).mockReturnValue(new Promise(() => {}));
        render(<ReportingPage />);
        expect(screen.getByTestId('loading')).toBeInTheDocument();
    });

    it('renders data after fetch', async () => {
        vi.mocked(api.fetchReporting).mockResolvedValue([
            { id: '1', tenantId: 't1', name: 'Report 1', status: 'PUBLISHED' }
        ]);
        render(<ReportingPage />);
        await waitFor(() => {
            expect(screen.getByText('Report 1')).toBeInTheDocument();
        });
    });
});
