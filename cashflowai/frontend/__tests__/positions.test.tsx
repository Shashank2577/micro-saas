import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import CashPositionsPage from '../src/app/positions/page'

describe('CashPositionsPage', () => {
  it('renders a heading', () => {
    render(<CashPositionsPage />)
    const heading = screen.getByRole('heading', { level: 1, name: /Cash Positions/i })
    expect(heading).toBeInTheDocument()
  })
})
