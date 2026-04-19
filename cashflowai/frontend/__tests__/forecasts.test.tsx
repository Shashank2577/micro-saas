import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import LiquidityForecastsPage from '../src/app/forecasts/page'

describe('LiquidityForecastsPage', () => {
  it('renders a heading', () => {
    render(<LiquidityForecastsPage />)
    const heading = screen.getByRole('heading', { level: 1, name: /Liquidity Forecasts/i })
    expect(heading).toBeInTheDocument()
  })
})
