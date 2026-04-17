import { render, screen, waitFor } from '@testing-library/react';
import App from './page';
import { vi } from 'vitest';
import axios from 'axios';

vi.mock('axios', () => {
    return {
        default: {
            create: vi.fn(() => ({
                get: vi.fn((url) => {
                    if (url === '/gateway/routes') {
                        return Promise.resolve({ data: [{ id: '1', routeId: 'route-1', path: '/api/v1', method: 'GET', targetUrl: 'http://test' }] });
                    }
                    if (url === '/analytics/traffic') {
                        return Promise.resolve({ data: [] });
                    }
                    return Promise.resolve({ data: [] });
                })
            }))
        }
    }
});

describe('Dashboard', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('renders dashboard layout and statistics', async () => {
        render(<App />);

        expect(screen.getByText('APIGatekeeper Dashboard')).toBeInTheDocument();
        expect(screen.getByText('Total Requests')).toBeInTheDocument();
        expect(screen.getByText('Active Routes')).toBeInTheDocument();
        expect(screen.getByText('Error Rate')).toBeInTheDocument();
        expect(screen.getByText('Avg Latency')).toBeInTheDocument();
        expect(screen.getByText('Configured Routes')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByText('route-1')).toBeInTheDocument();
        });
    });
});
