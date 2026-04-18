import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import LoginPage from '../app/login/page';

describe('LoginPage', () => {
    it('renders login form', () => {
        render(<LoginPage />);
        expect(screen.getByText('Login')).toBeInTheDocument();
        expect(screen.getByLabelText('Email')).toBeInTheDocument();
        expect(screen.getByLabelText('Password')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument();
    });
});
