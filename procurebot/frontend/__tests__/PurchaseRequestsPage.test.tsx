import { render, screen } from '@testing-library/react';
import PurchaseRequestsPage from '../src/app/procurement/requests/page';

describe('PurchaseRequestsPage', () => {
    it('renders the heading', () => {
        render(<PurchaseRequestsPage />);
        expect(screen.getByRole('heading', { name: /PurchaseRequests/i })).toBeDefined();
    });
});
