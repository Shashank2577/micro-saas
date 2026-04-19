import { render, screen } from '@testing-library/react';
import VendorOffersPage from '../src/app/procurement/offers/page';

describe('VendorOffersPage', () => {
    it('renders the heading', () => {
        render(<VendorOffersPage />);
        expect(screen.getByRole('heading', { name: /VendorOffers/i })).toBeDefined();
    });
});
