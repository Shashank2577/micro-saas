import { render, screen } from '@testing-library/react'
import Page from '../app/page'

describe('Dashboard Page', () => {
  it('renders heading', () => {
    render(<Page />)
    const heading = screen.getByRole('heading', { name: /AgencyOS Dashboard/i })
    expect(heading).toBeDefined()
  })
})
