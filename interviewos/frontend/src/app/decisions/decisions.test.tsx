import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import DecisionPacketPage from './page';

describe('Decision Packets Page', () => {
    it('renders heading', async () => {
        render(<DecisionPacketPage />);
        expect(screen.getByText('Decision Packets')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByTestId('decisions-table')).toBeInTheDocument();
        });

        expect(screen.getByText('Test Decision Packets')).toBeInTheDocument();
    });
});
