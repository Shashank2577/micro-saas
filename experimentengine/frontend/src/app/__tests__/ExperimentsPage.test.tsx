import React from "react";
import { render, screen, waitFor } from '@testing-library/react'
import ExperimentsPage from '../(dashboard)/experiments/page'
import { describe, it, expect, vi } from 'vitest'

global.fetch = vi.fn() as any

describe('ExperimentsPage', () => {
  it('renders experiments successfully', async () => {
    const mockExperiments = [
      { id: '1', name: 'Test A', status: 'RUNNING', hypothesis: 'h1' }
    ]
    ;(global.fetch as any).mockResolvedValueOnce({
      json: async () => mockExperiments,
    })

    render(<ExperimentsPage />)

    expect(screen.getByText('Experiments')).toBeInTheDocument()

    await waitFor(() => {
      expect(screen.getByText('Test A')).toBeInTheDocument()
    })
  })
})
