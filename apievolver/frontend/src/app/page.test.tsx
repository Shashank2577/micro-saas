import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import DashboardPage from './page';
import '@testing-library/jest-dom';

describe('DashboardPage', () => {
  it('renders the dashboard with correct links', async () => {
    // Await the async component render
    const resolvedComponent = await DashboardPage();
    render(resolvedComponent);

    expect(screen.getByText('APIEvolver')).toBeInTheDocument();

    // Check for standard cards
    expect(screen.getByText('API Specs')).toBeInTheDocument();
    expect(screen.getByText('API Versions')).toBeInTheDocument();
    expect(screen.getByText('Breaking Changes')).toBeInTheDocument();
    expect(screen.getByText('Compatibility Reports')).toBeInTheDocument();
    expect(screen.getByText('Deprecation Notices')).toBeInTheDocument();
    expect(screen.getByText('SDK Artifacts')).toBeInTheDocument();
  });
});
