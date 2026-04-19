import { render, screen } from '@testing-library/react';
import ProcurementEventsPage from '../src/app/procurement/events/page';

describe('ProcurementEventsPage', () => {
    it('renders the heading', () => {
        render(<ProcurementEventsPage />);
        expect(screen.getByRole('heading', { name: /ProcurementEvents/i })).toBeDefined();
    });
});
