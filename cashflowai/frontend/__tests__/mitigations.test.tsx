import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import MitigationOptionsPage from '../src/app/mitigations/page'

describe('MitigationOptionsPage', () => {
  it('renders a heading', () => {
    render(<MitigationOptionsPage />)
    const heading = screen.getByRole('heading', { level: 1, name: /Mitigation Options/i })
    expect(heading).toBeInTheDocument()
  })
})
