import { render, screen } from '@testing-library/react'
import Page from './page'

describe('DashboardPage', () => {
  it('renders a heading', () => {
    render(<Page />)

    const heading = screen.getByRole('heading', {
      name: /HealthcareDocAI Dashboard/i,
    })

    expect(heading).toBeInTheDocument()
  })
})
