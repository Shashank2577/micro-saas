import { render, screen, fireEvent } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import Home from './page'

// Mock react-dropzone to avoid testing its internal logic
vi.mock('react-dropzone', () => ({
  useDropzone: () => ({
    getRootProps: () => ({}),
    getInputProps: () => ({}),
    isDragActive: false
  })
}))

describe('Home', () => {
  it('renders the page title', () => {
    render(<Home />)
    expect(screen.getAllByText('Document Intelligence')[0]).toBeInTheDocument()
    expect(screen.getByText('Drag & drop documents here')).toBeInTheDocument()
  })

  it('switches tabs', () => {
    render(<Home />)
    const searchTab = screen.getAllByText('Semantic Search')[0]
    fireEvent.click(searchTab)
    expect(screen.getByPlaceholderText('Ask a question or search for concepts across all documents...')).toBeInTheDocument()
  })
})
