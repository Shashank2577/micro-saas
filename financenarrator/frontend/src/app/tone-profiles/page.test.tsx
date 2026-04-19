import { describe, it, expect } from "vitest";
import { render, screen } from '@testing-library/react';
import ToneProfilesPage from './page';
import * as api from '../../lib/api/tone-profiles';
import { vi } from 'vitest';

vi.mock('../../lib/api/tone-profiles', () => ({
  fetchToneProfiles: vi.fn().mockResolvedValue([
    { id: '1', name: 'Profile 1', status: 'DRAFT' }
  ])
}));

describe('ToneProfilesPage', () => {
  it('renders correctly', async () => {
    render(<ToneProfilesPage />);
    expect(await screen.findByText('Tone Profiles')).toBeInTheDocument();
    expect(await screen.findByText('Profile 1')).toBeInTheDocument();
  });
});
