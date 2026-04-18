import { render, screen } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Dashboard from './page'

const queryClient = new QueryClient()

describe('Dashboard', () => {
  it('renders loading state initially', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <Dashboard />
      </QueryClientProvider>
    )
    expect(document.querySelector('.animate-spin')).toBeInTheDocument()
  })
})
