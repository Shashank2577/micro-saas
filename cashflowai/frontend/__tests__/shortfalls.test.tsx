import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import ShortfallAlertsPage from '../src/app/shortfalls/page'

describe('ShortfallAlertsPage', () => {
  it('renders a heading', () => {
    render(<ShortfallAlertsPage />)
    const heading = screen.getByRole('heading', { level: 1, name: /Shortfall Alerts/i })
    expect(heading).toBeInTheDocument()
  })
})
