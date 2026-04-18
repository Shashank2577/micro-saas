import { render, screen } from '@testing-library/react'
import { expect, test } from 'vitest'
import Home from '../page'

test('Home page renders dashboard title', () => {
  render(<Home />)
  expect(screen.getByText('NonprofitOS Dashboard')).toBeInTheDocument()
})

test('Home page renders navigation links', () => {
  render(<Home />)
  expect(screen.getByText('Donors')).toBeInTheDocument()
  expect(screen.getByText('Grants')).toBeInTheDocument()
  expect(screen.getByText('Impact')).toBeInTheDocument()
})
