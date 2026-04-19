import { render, screen } from '@testing-library/react';
import ApprovalFlowsPage from '../src/app/procurement/approvals/page';

describe('ApprovalFlowsPage', () => {
    it('renders the heading', () => {
        render(<ApprovalFlowsPage />);
        expect(screen.getByRole('heading', { name: /ApprovalFlows/i })).toBeDefined();
    });
});
