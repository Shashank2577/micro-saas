import { render, screen, waitFor } from '@testing-library/react';
import InsightsPage from '../app/insights/page';
import * as api from '../lib/api';
import { vi } from 'vitest';

vi.mock('../lib/api');

describe('InsightsPage', () => {
    it('renders loading state initially', () => {
        vi.mocked(api.fetchInsights).mockReturnValue(new Promise(() => {}));
        render(<InsightsPage />);
        expect(screen.getByTestId('loading')).toBeInTheDocument();
    });

    it('renders data after fetch', async () => {
        vi.mocked(api.fetchInsights).mockResolvedValue([
            { id: '1', tenantId: 't1', name: 'Insight 1', status: 'ACTIVE' }
        ]);
        render(<InsightsPage />);
        await waitFor(() => {
            expect(screen.getByText('Insight 1')).toBeInTheDocument();
        });
    });
});
