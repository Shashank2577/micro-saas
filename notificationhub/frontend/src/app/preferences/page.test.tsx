import { render, screen } from '@testing-library/react';
import { test, expect } from 'vitest';
import PreferencesPage from './page';

test('renders preferences page', () => {
  render(<PreferencesPage />);
  expect(screen.getByText('Notification Preferences')).toBeDefined();
});
