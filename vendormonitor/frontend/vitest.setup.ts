import '@testing-library/jest-dom/vitest'
import { vi } from 'vitest'
import React from 'react'

global.React = React

// Mock Chart.js to prevent canvas errors
vi.mock('react-chartjs-2', () => ({
  Line: () => null,
  Bar: () => null,
  Doughnut: () => null,
  Pie: () => null,
}))
