import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import ScenarioRunsPage from '../src/app/scenarios/page'

describe('ScenarioRunsPage', () => {
  it('renders a heading', () => {
    render(<ScenarioRunsPage />)
    const heading = screen.getByRole('heading', { level: 1, name: /Scenario Runs/i })
    expect(heading).toBeInTheDocument()
  })
})
