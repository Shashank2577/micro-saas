import { render, screen, waitFor } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'

import OrgSnapshotsPage from '../app/org-snapshots/page'
import HeadcountMetricsPage from '../app/headcount-metrics/page'
import AttritionSignalsPage from '../app/attrition-signals/page'
import EngagementIndicatorsPage from '../app/engagement-indicators/page'
import PerformanceTrendsPage from '../app/performance-trends/page'
import PlanningScenariosPage from '../app/planning-scenarios/page'

global.fetch = vi.fn()

describe('Frontend Pages', () => {
  beforeEach(() => {
    vi.mocked(global.fetch).mockResolvedValue({
      json: async () => [{ id: '1', name: 'Test Item', status: 'ACTIVE' }]
    } as Response)
  })

  it('renders OrgSnapshotsPage', async () => {
    render(<OrgSnapshotsPage />)
    expect(screen.getByTestId('loading')).toBeInTheDocument()
    await waitFor(() => {
      expect(screen.getByText('Test Item', { exact: false })).toBeInTheDocument()
    })
  })

  it('renders HeadcountMetricsPage', async () => {
    render(<HeadcountMetricsPage />)
    expect(screen.getByTestId('loading')).toBeInTheDocument()
    await waitFor(() => {
      expect(screen.getByText('Test Item - ACTIVE')).toBeInTheDocument()
    })
  })

  it('renders AttritionSignalsPage', async () => {
    render(<AttritionSignalsPage />)
    expect(screen.getByTestId('loading')).toBeInTheDocument()
    await waitFor(() => {
      expect(screen.getByText('Test Item - ACTIVE')).toBeInTheDocument()
    })
  })

  it('renders EngagementIndicatorsPage', async () => {
    render(<EngagementIndicatorsPage />)
    expect(screen.getByTestId('loading')).toBeInTheDocument()
    await waitFor(() => {
      expect(screen.getByText('Test Item - ACTIVE')).toBeInTheDocument()
    })
  })

  it('renders PerformanceTrendsPage', async () => {
    render(<PerformanceTrendsPage />)
    expect(screen.getByTestId('loading')).toBeInTheDocument()
    await waitFor(() => {
      expect(screen.getByText('Test Item - ACTIVE')).toBeInTheDocument()
    })
  })

  it('renders PlanningScenariosPage', async () => {
    render(<PlanningScenariosPage />)
    expect(screen.getByTestId('loading')).toBeInTheDocument()
    await waitFor(() => {
      expect(screen.getByText('Test Item - ACTIVE')).toBeInTheDocument()
    })
  })
})
