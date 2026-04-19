import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import FundingEventsPage from '../src/app/events/page'

describe('FundingEventsPage', () => {
  it('renders a heading', () => {
    render(<FundingEventsPage />)
    const heading = screen.getByRole('heading', { level: 1, name: /Funding Events/i })
    expect(heading).toBeInTheDocument()
  })
})
