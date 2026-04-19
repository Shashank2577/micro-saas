import { describe, it, expect, vi, beforeEach } from 'vitest'
import { fetchWithTenant, API_BASE_URL, DEFAULT_TENANT_ID } from '../apiClient'

global.fetch = vi.fn()

describe('apiClient', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('fetchWithTenant includes X-Tenant-ID header', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      status: 200,
      json: async () => ({ data: 'test' })
    })

    await fetchWithTenant('/test-endpoint')

    expect(global.fetch).toHaveBeenCalledWith(
      `${API_BASE_URL}/test-endpoint`,
      expect.objectContaining({
        headers: expect.objectContaining({
          'X-Tenant-ID': DEFAULT_TENANT_ID,
          'Content-Type': 'application/json'
        })
      })
    )
  })
})
