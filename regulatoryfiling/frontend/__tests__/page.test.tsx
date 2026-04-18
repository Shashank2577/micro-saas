import { render, screen } from '@testing-library/react'
import { expect, test } from 'vitest'
import Home from '../src/app/page'

test('renders dashboard heading', () => {
  render(<Home />)
  expect(screen.getByText('RegulatoryFiling Dashboard')).toBeDefined()
})
