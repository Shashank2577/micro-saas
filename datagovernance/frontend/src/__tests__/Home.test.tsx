import { render, screen } from '@testing-library/react';
import { describe, it, expect, afterEach } from 'vitest';
import { cleanup } from '@testing-library/react';
import Home from '@/app/page';

describe('Home Page', () => {
  afterEach(() => {
    cleanup();
  });

  it('renders dashboard heading', () => {
    render(<Home />);
    expect(screen.getByRole('heading', { name: 'Data Governance Dashboard' })).toBeInTheDocument();
  });
  
  it('renders links to all modules', () => {
    render(<Home />);
    expect(screen.getByRole('heading', { name: 'Retention Policies' })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'DSAR Requests' })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'Consent Management' })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'Data Lineage' })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'Audit Logs' })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'PII Detection' })).toBeInTheDocument();
  });
});
