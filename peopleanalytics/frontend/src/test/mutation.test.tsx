import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import OrgSnapshotsPage from '../app/org-snapshots/page'

global.fetch = vi.fn()

describe('Frontend Mutation Flow', () => {
  beforeEach(() => {
    vi.mocked(global.fetch).mockImplementation((url, options) => {
      if (url === '/api/v1/people-analytics/org-snapshots' && options?.method === 'POST') {
        return Promise.resolve({
          json: async () => ({ id: '2', name: 'New Item', status: 'ACTIVE' })
        } as Response)
      }
      return Promise.resolve({
        json: async () => [{ id: '1', name: 'Test Item', status: 'ACTIVE' }]
      } as Response)
    })
  })

  it('can create a new org snapshot', async () => {
    render(<OrgSnapshotsPage />)

    // Wait for initial load
    await waitFor(() => {
      expect(screen.getByText('Test Item', { exact: false })).toBeInTheDocument()
    })

    // Fill form
    fireEvent.change(screen.getByTestId('input-name'), { target: { value: 'New Item' } })
    fireEvent.click(screen.getByTestId('submit-btn'))

    // Verify fetch was called correctly
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith('/api/v1/people-analytics/org-snapshots', expect.objectContaining({
        method: 'POST',
        body: expect.stringContaining('New Item')
      }))
    })
  })
})
