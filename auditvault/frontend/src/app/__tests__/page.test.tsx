import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import DashboardPage from '../page';
import { api } from '@/lib/api';

vi.mock('@/lib/api', () => ({
    api: {
        frameworks: {
            list: vi.fn().mockResolvedValue([{ id: '1', name: 'SOC 2', status: 'ACTIVE', description: 'SOC 2 Framework' }])
        },
        evidence: {
            list: vi.fn().mockResolvedValue([{ id: '1', sourceApp: 'TestApp', status: 'PENDING_MAPPING', content: 'Test content' }]),
            map: vi.fn()
        },
        packages: {
            list: vi.fn().mockResolvedValue([]),
            generate: vi.fn()
        }
    }
}));

describe('DashboardPage', () => {
    it('renders dashboard with frameworks and evidence', async () => {
        render(<DashboardPage />);
        expect(screen.getByText('Loading...')).toBeInTheDocument();
        
        await waitFor(() => {
            expect(screen.getByText('AuditVault Dashboard')).toBeInTheDocument();
        });

        expect(screen.getAllByText('SOC 2').length).toBeGreaterThan(0);
        expect(screen.getByText(/TestApp/)).toBeInTheDocument();
        expect(screen.getByText('Generate Audit Package')).toBeInTheDocument();
    });
});
