import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Dashboard from '../src/app/performance/page';

describe('Performance Dashboard', () => {
    it('renders the dashboard with proper links', () => {
        render(<Dashboard />);
        expect(screen.getByText('Performance Dashboard')).toBeInTheDocument();
        expect(screen.getByText('Review Cycles')).toBeInTheDocument();
        expect(screen.getByText('Employee Reviews')).toBeInTheDocument();
    });
});
