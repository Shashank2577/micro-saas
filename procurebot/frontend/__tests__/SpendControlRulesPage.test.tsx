import { render, screen } from '@testing-library/react';
import SpendControlRulesPage from '../src/app/procurement/controls/page';

describe('SpendControlRulesPage', () => {
    it('renders the heading', () => {
        render(<SpendControlRulesPage />);
        expect(screen.getByRole('heading', { name: /SpendControlRules/i })).toBeDefined();
    });
});
