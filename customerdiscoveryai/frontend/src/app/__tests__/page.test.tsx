import { render, screen, waitFor } from '@testing-library/react'
import { vi, describe, it, expect, beforeEach } from 'vitest'
import Home from '../page'
import { api } from '@/lib/api'

vi.mock('@/lib/api', () => ({
  api: {
    get: vi.fn(),
  },
}))

describe('Home Page', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders loading state initially', () => {
    (api.get as any).mockImplementation(() => new Promise(() => {}))
    render(<Home />)
    expect(screen.getByText('Loading projects...')).toBeInTheDocument()
  })

  it('renders a list of projects', async () => {
    const mockProjects = [
      { id: '1', name: 'Project Alpha', description: 'Desc 1', targetAudience: 'PMs' },
      { id: '2', name: 'Project Beta', description: 'Desc 2', targetAudience: 'Devs' },
    ]
    ;(api.get as any).mockResolvedValueOnce({ data: mockProjects })

    render(<Home />)

    await waitFor(() => {
      expect(screen.queryByText('Loading projects...')).not.toBeInTheDocument()
    })

    expect(screen.getByText('Customer Discovery AI')).toBeInTheDocument()
    expect(screen.getByText('Project Alpha')).toBeInTheDocument()
    expect(screen.getByText('Desc 1')).toBeInTheDocument()
    expect(screen.getByText('Project Beta')).toBeInTheDocument()
  })

  it('renders empty state when no projects', async () => {
    ;(api.get as any).mockResolvedValueOnce({ data: [] })

    render(<Home />)

    await waitFor(() => {
      expect(screen.getByText('No research projects found. Create one to get started.')).toBeInTheDocument()
    })
  })
})
