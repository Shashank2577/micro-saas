import { render, screen, waitFor, fireEvent } from '@testing-library/react'
import { vi, describe, it, expect, beforeEach } from 'vitest'
import ProjectDetails from '../page'
import { api } from '@/lib/api'

vi.mock('@/lib/api', () => ({
  api: {
    get: vi.fn(),
    post: vi.fn(),
  },
}))

vi.mock('next/navigation', () => ({
  useParams: () => ({ id: '123' }),
}))

describe('Project Details Page', () => {
  const mockProject = { id: '123', name: 'Test Project', description: 'Test Desc' }
  const mockInterviews = [
    { id: 'int-1', participantName: 'Alice', participantEmail: 'alice@test.com', status: 'COMPLETED', transcript: 'Hello' }
  ]
  const mockInsights = [
    { id: 'ins-1', theme: 'Usability', description: 'Hard to use', confidenceScore: 0.85, supportingQuotes: '["Too many clicks"]' }
  ]
  const mockReports = [
    { id: 'rep-1', title: 'Q1 Report', content: '# Summary\nThings are good.' }
  ]

  beforeEach(() => {
    vi.clearAllMocks()
    ;(api.get as any).mockImplementation((url: string) => {
      if (url.includes('/interviews')) return Promise.resolve({ data: mockInterviews })
      if (url.includes('/insights')) return Promise.resolve({ data: mockInsights })
      if (url.includes('/reports')) return Promise.resolve({ data: mockReports })
      return Promise.resolve({ data: mockProject })
    })
  })

  it('renders project details and tabs', async () => {
    render(<ProjectDetails />)

    await waitFor(() => {
      expect(screen.queryByText('Loading project details...')).not.toBeInTheDocument()
    })

    expect(screen.getByText('Test Project')).toBeInTheDocument()
    expect(screen.getByText('Test Desc')).toBeInTheDocument()
    
    expect(screen.getByText('Interviews (1)')).toBeInTheDocument()
    expect(screen.getByText('Insights (1)')).toBeInTheDocument()
    expect(screen.getByText('Reports (1)')).toBeInTheDocument()
  })

  it('displays interviews tab content initially', async () => {
    render(<ProjectDetails />)
    await waitFor(() => expect(screen.getByText('Alice')).toBeInTheDocument())
    expect(screen.getByText('alice@test.com')).toBeInTheDocument()
    expect(screen.getByText('COMPLETED')).toBeInTheDocument()
  })

  it('switches to insights tab and handles synthesis', async () => {
    ;(api.post as any).mockResolvedValueOnce({ data: mockInsights })
    
    render(<ProjectDetails />)
    await waitFor(() => expect(screen.getByText('Interviews (1)')).toBeInTheDocument())
    
    const synthesizeBtn = screen.getByText('Synthesize Insights')
    fireEvent.click(synthesizeBtn)
    
    await waitFor(() => expect(api.post).toHaveBeenCalledWith('/api/projects/123/synthesize'))
    expect(screen.getByText('Usability')).toBeInTheDocument()
    expect(screen.getByText('Hard to use')).toBeInTheDocument()
    expect(screen.getByText('Confidence: 85%')).toBeInTheDocument()
  })

  it('switches to reports tab and handles generation', async () => {
    const newReport = { id: 'rep-2', title: 'New Report', content: 'New content' }
    ;(api.post as any).mockResolvedValueOnce({ data: newReport })
    
    render(<ProjectDetails />)
    await waitFor(() => expect(screen.getByText('Interviews (1)')).toBeInTheDocument())
    
    const insightsTab = screen.getByText('Insights (1)')
    fireEvent.click(insightsTab)
    
    await waitFor(() => expect(screen.getByText('Usability')).toBeInTheDocument())
    
    const generateBtn = screen.getByText('Generate Report')
    fireEvent.click(generateBtn)
    
    await waitFor(() => expect(api.post).toHaveBeenCalledWith('/api/projects/123/reports'))
    expect(screen.getByText('New Report')).toBeInTheDocument()
    expect(screen.getByText('New content')).toBeInTheDocument()
  })
})
