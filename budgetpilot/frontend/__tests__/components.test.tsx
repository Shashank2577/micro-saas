import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { VarianceAlert } from '../components/VarianceAlert';
import { BudgetTable } from '../components/BudgetTable';
import React from 'react';

describe('VarianceAlert', () => {
    it('renders explanation text', () => {
        render(<VarianceAlert explanation="Test overrun explanation" />);
        expect(screen.getByText('Test overrun explanation')).toBeInTheDocument();
    });
});

describe('BudgetTable', () => {
    it('renders budget items correctly', () => {
        const items = [
            {
                id: '1',
                category: 'Software',
                department: 'Eng',
                allocatedAmount: 100,
                actualAmount: 150
            }
        ];
        render(<BudgetTable items={items} />);
        expect(screen.getByText('Software')).toBeInTheDocument();
        expect(screen.getByText('Eng')).toBeInTheDocument();
        expect(screen.getByText('$100')).toBeInTheDocument();
        expect(screen.getByText('$150')).toBeInTheDocument();
    });
});
