import { render, screen } from '@testing-library/react';
import { test, expect } from 'vitest';
import Home from './page';

test('renders dashboard page', () => {
  render(<Home />);
  expect(screen.getByText('NotificationHub Dashboard')).toBeDefined();
});
