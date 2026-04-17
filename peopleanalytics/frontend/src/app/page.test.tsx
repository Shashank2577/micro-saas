import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import DashboardPage from './page'

describe('DashboardPage', () => {
  it('renders the dashboard heading', () => {
    render(<DashboardPage />)
    expect(screen.getByText('PeopleAnalytics Dashboard')).toBeInTheDocument()
  })
})
