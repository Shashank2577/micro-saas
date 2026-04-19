import { render, screen, act } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import Home from './page'
import api from '@/lib/api'

vi.mock('@/lib/api', () => ({
  default: {
    get: vi.fn()
  }
}))

describe('Dashboard Page', () => {
  it('renders dashboard with stats', async () => {
    vi.mocked(api.get).mockImplementation((url) => {
      if (url === '/frameworks') return Promise.resolve({ data: [{ id: '1' }] })
      if (url === '/controls') return Promise.resolve({ data: [{ id: '1' }, { id: '2' }] })
      if (url === '/evidence') return Promise.resolve({ data: [{ id: '1' }, { id: '2' }, { id: '3' }] })
      if (url === '/gaps') return Promise.resolve({ data: [{ id: '1' }, { id: '2' }, { id: '3' }, { id: '4' }] })
      return Promise.resolve({ data: [] })
    })

    await act(async () => {
      render(<Home />)
    })

    expect(screen.getByText('Dashboard')).toBeInTheDocument()
    expect(screen.getByText('1')).toBeInTheDocument() // Frameworks
    expect(screen.getByText('2')).toBeInTheDocument() // Controls
    expect(screen.getByText('3')).toBeInTheDocument() // Evidence
    expect(screen.getByText('4')).toBeInTheDocument() // Gaps
  })
})
