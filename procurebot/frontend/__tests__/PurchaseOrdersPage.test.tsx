import { render, screen } from '@testing-library/react';
import PurchaseOrdersPage from '../src/app/procurement/orders/page';

describe('PurchaseOrdersPage', () => {
    it('renders the heading', () => {
        render(<PurchaseOrdersPage />);
        expect(screen.getByRole('heading', { name: /PurchaseOrders/i })).toBeDefined();
    });
});
