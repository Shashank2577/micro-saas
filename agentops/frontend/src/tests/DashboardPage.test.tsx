import { render, screen, waitFor } from '@testing-library/react'
import { expect, test, vi } from 'vitest'
import DashboardPage from '../app/page'
import * as api from '../lib/api'

vi.mock('../lib/api')

test('DashboardPage renders metrics', async () => {
  vi.mocked(api.getHealthMetrics).mockResolvedValue([
    {
      id: '1',
      agentId: 'agent-1',
      periodStart: '2026-04-01T00:00:00Z',
      periodEnd: '2026-04-30T23:59:59Z',
      successRate: 98.5,
      avgCost: 0.05,
      avgLatencyMs: 1200,
      escalationRate: 1.2
    }
  ])

  render(<DashboardPage />)
  
  expect(screen.getByText('Dashboard')).toBeInTheDocument()
  
  await waitFor(() => {
    expect(screen.getByText('agent-1')).toBeInTheDocument()
    expect(screen.getByText('98.5%')).toBeInTheDocument()
    expect(screen.getByText('1200ms')).toBeInTheDocument()
    expect(screen.getByText('1.2%')).toBeInTheDocument()
  })
})
