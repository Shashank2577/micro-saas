import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import RegisterPage from '../app/register/page';

describe('RegisterPage', () => {
    it('renders register form', () => {
        render(<RegisterPage />);
        expect(screen.getByText('Register')).toBeInTheDocument();
        expect(screen.getByLabelText('Email')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /sign up/i })).toBeInTheDocument();
    });
});
