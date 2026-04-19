import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import Home from '../page'

describe('Home Page', () => {
  it('renders heading', () => {
    render(<Home />)
    const heading = screen.getByRole('heading', { name: /Creator Analytics/i })
    expect(heading).toBeInTheDocument()
  })

  it('renders links', () => {
    render(<Home />)
    expect(screen.getByText('Content Assets')).toBeInTheDocument()
    expect(screen.getByText('Channel Metrics')).toBeInTheDocument()
    expect(screen.getByText('Attribution Models')).toBeInTheDocument()
    expect(screen.getByText('ROI Snapshots')).toBeInTheDocument()
    expect(screen.getByText('Audience Segments')).toBeInTheDocument()
    expect(screen.getByText('Performance Insights')).toBeInTheDocument()
  })
})
